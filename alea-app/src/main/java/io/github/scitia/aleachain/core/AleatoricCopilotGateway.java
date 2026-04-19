package io.github.scitia.aleachain.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.ConnectionState;
import com.github.copilot.sdk.CopilotClient;
import com.github.copilot.sdk.CopilotSession;
import com.github.copilot.sdk.json.SessionConfig;
import com.github.copilot.sdk.json.ToolDefinition;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.aleachain.api.AleatoricCommandRequest;
import io.github.scitia.aleachain.api.AleatoricCommandResponse;
import io.github.scitia.aleachain.tools.QuoteEvaluateToolHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class AleatoricCopilotGateway {

    private final ObjectMapper objectMapper;
    private final FlowEngine flowEngine;
    private final QuoteEvaluateToolHandler quoteEvaluateToolHandler;

    private volatile CopilotClient client;
    private volatile CopilotSession session;

    public AleatoricCopilotGateway(ObjectMapper objectMapper, FlowEngine flowEngine) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
        this.quoteEvaluateToolHandler = new QuoteEvaluateToolHandler(this.flowEngine, this.objectMapper);
    }

    public AleatoricCommandResponse execute(AleatoricCommandRequest request) {
        validate(request);
        CopilotSession activeSession = ensureSession();

        ToolInvocation invocation = new ToolInvocation()
                .setSessionId(activeSession.getSessionId())
                .setToolName("quote_evaluate")
                .setArguments(objectMapper.valueToTree(request.payload()));
        Object processResult = quoteEvaluateToolHandler.invoke(invocation).join();
        return new AleatoricCommandResponse(activeSession.getSessionId(), processResult);
    }

    private synchronized CopilotSession ensureSession() {
        if (session != null) {
            return session;
        }

        if (client == null) {
            client = new CopilotClient();
        }

        if (client.getState() != ConnectionState.CONNECTED) {
            client.start().join();
        }

        ToolDefinition quoteEvaluateTool = ToolDefinition.create(
                "quote_evaluate",
                "Evaluate quote request and return business decision",
                quoteToolSchema(),
            quoteEvaluateToolHandler
        );

        SessionConfig config = new SessionConfig()
                .setClientName("alea-app-aleatoric-chain")
                .setTools(List.of(quoteEvaluateTool));

        session = client.createSession(config).join();
        return session;
    }

    private Map<String, Object> quoteToolSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "customerId", Map.of("type", "string"),
                        "units", Map.of("type", "integer"),
                        "unitPrice", Map.of("type", "number"),
                        "premiumCustomer", Map.of("type", "boolean"),
                        "expedited", Map.of("type", "boolean")
                ),
                "required", List.of("customerId", "units", "unitPrice", "premiumCustomer", "expedited")
        );
    }

    private String buildPrompt(AleatoricCommandRequest request) {
        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(request.payload());
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Payload could not be serialized to JSON", exception);
        }

        return """
                You are executing Aleatoric Execution Chain in a server-side app.
                Decide what to execute based on request intent and payload.
                If this is a quote evaluation, you MUST call tool 'quote_evaluate'.
                Return a concise summary with decision details.

                intent: %s
                instructions: %s
                payload: %s
                metadata: %s
                """.formatted(
                safe(request.intent()),
                safe(request.instructions()),
                payloadJson,
                request.metadata() == null ? "{}" : request.metadata().toString()
        );
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }

    private static void validate(AleatoricCommandRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Aleatoric command request cannot be null");
        }
        if (request.payload() == null || request.payload().isEmpty()) {
            throw new IllegalArgumentException("Aleatoric command payload cannot be null or empty");
        }
    }

    @PreDestroy
    public synchronized void shutdown() {
        if (session != null) {
            session.close();
            session = null;
        }
        if (client != null) {
            CompletableFuture<Void> stopFuture = client.stop();
            stopFuture.join();
            client = null;
        }
    }
}
