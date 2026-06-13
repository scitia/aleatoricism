package io.github.scitia.config.aleatoricism.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.CopilotClient;
import com.github.copilot.sdk.events.SessionIdleEvent;
import com.github.copilot.sdk.json.MessageOptions;
import com.github.copilot.sdk.json.PermissionHandler;
import com.github.copilot.sdk.json.SessionConfig;
import com.github.copilot.sdk.json.ToolDefinition;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandRequest;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandResponse;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Component
public class AleatoricCopilotGateway {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator;
    private final List<AgenticBusinessTool> businessTools;

    public AleatoricCopilotGateway(Validator validator, List<AgenticBusinessTool> businessTools) {
        this.validator = validator;
        this.businessTools = List.copyOf(businessTools);
        this.businessTools.stream().map(AgenticBusinessTool::toolName).forEach(toolName ->
                log.info("Registered business tool for aleatoric gateway: {}", toolName)
        );
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
            session.on(SessionIdleEvent.class, _ -> done.complete(null));
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

        Object processResult = executions.getLast().result();
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
        } catch (Exception _) {
            payloadJson = String.valueOf(request.payload());
            metadataJson = String.valueOf(request.metadata());
        }


        return """
                You are an AI agent that MUST use one of the available tools to handle the request.
                
                AVAILABLE TOOLS:
                %s
                
                USER INTENT:
                %s
                
                INPUT DATA:
                %s
                
                IMPORTANT:
                - You MUST call a tool
                - Do NOT answer directly
                - Select the most appropriate tool
                - Return ONLY tool execution
                
                """.formatted(
                toolsSummary,
                safeText(request.intent()),
                payloadJson
        );
    }

    private static String safeText(String value) {
        return value == null ? "" : value;
    }

    private record ToolExecution(String toolName, Object result) {
    }
}
