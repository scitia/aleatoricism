package io.github.scitia.config.aleatoricism.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.CopilotClient;
import com.github.copilot.sdk.events.SessionIdleEvent;
import com.github.copilot.sdk.json.MessageOptions;
import com.github.copilot.sdk.json.PermissionHandler;
import com.github.copilot.sdk.json.SessionConfig;
import com.github.copilot.sdk.json.ToolDefinition;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandRequest;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Validated
@Component
public class AleatoricCopilotGateway {

    private final ObjectMapper objectMapper = new ObjectMapper();;
    private final Validator validator;
    private final List<AgenticBusinessTool> businessTools;

    public AleatoricCopilotGateway(Validator validator, List<AgenticBusinessTool> businessTools) {
        this.validator = validator;
        this.businessTools = List.copyOf(businessTools);
    }

    public AleatoricCommandResponse execute(@Valid AleatoricCommandRequest request) {
        Set<ConstraintViolation<AleatoricCommandRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (businessTools.isEmpty()) {
            throw new IllegalStateException("No business tools registered for aleatoric gateway");
        }

        List<ToolExecution> executions = Collections.synchronizedList(new ArrayList<>());
        List<ToolDefinition> toolDefinitions = businessTools.stream()
                .map(tool -> ToolDefinition.create(
                        tool.toolName(),
                        tool.description(),
                        tool.inputSchema(),
                        invocation -> tool.invoke(invocation)
                                .thenApply(result -> {
                                    executions.add(new ToolExecution(tool.toolName(), result));
                                    return result;
                                })
                ))
                .toList();

        try (var client = new CopilotClient()) {
            client.start().get();

            var session = client.createSession(
                    new SessionConfig()
                            .setOnPermissionRequest(PermissionHandler.APPROVE_ALL)
                            .setTools(toolDefinitions)
                            .setModel("gpt-4.1"))
                    .get();

            var done = new CompletableFuture<Void>();
            session.on(SessionIdleEvent.class, idle -> done.complete(null));

            session.send(new MessageOptions().setPrompt(buildAgentPrompt(request))).get();
            done.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Aleatoric chain execution interrupted", exception);
        } catch (ExecutionException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new IllegalStateException("Aleatoric chain execution failed", exception);
        }

        if (executions.isEmpty()) {
            throw new IllegalStateException("Agent did not invoke any business tool");
        }

        Object processResult = executions.get(executions.size() - 1).result();
        if (executions.size() > 1) {
            processResult = Map.of(
                    "selectedTools", executions.stream().map(ToolExecution::toolName).toList(),
                    "result", processResult
            );
        }

        String sessionId = String.valueOf(request.metadata().getOrDefault("sessionId", "agentic-session"));
        return new AleatoricCommandResponse(sessionId, processResult);
    }

    private String buildAgentPrompt(AleatoricCommandRequest request) {
        String toolsSummary = businessTools.stream()
                .map(tool -> "- " + tool.toolName() + ": " + tool.description())
                .collect(Collectors.joining("\\n"));
        String payloadJson;
        String metadataJson;
        try {
            payloadJson = objectMapper.writeValueAsString(request.payload());
            metadataJson = objectMapper.writeValueAsString(request.metadata());
        } catch (Exception exception) {
            payloadJson = String.valueOf(request.payload());
            metadataJson = String.valueOf(request.metadata());
        }

        return """
            You are an execution planner for business tools.
            Choose tool(s) from the catalog and invoke them with proper JSON arguments.
            You may call multiple tools if needed, but only if each call adds business value.
            If no tool can safely satisfy intent, fail with a clear exception.

            Tool catalog:
            %s

            Intent: %s
            Instructions: %s
            Payload: %s
            Metadata: %s
            """.formatted(
                toolsSummary,
                safeText(request.intent()),
                safeText(request.instructions()),
                payloadJson,
                metadataJson
        );
    }

    private static String safeText(String value) {
        return value == null ? "" : value;
    }

    private record ToolExecution(String toolName, Object result) {
    }
}
