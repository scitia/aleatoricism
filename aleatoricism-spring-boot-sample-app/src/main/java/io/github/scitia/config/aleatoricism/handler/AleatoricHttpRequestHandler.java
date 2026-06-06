package io.github.scitia.config.aleatoricism.handler;

import io.github.scitia.aleatoricism.chain.HttpRequestContext;
import io.github.scitia.aleatoricism.chain.HttpRequestHandler;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandRequest;
import io.github.scitia.config.aleatoricism.command.AleatoricCommandResponse;
import io.github.scitia.config.aleatoricism.core.AleatoricCopilotGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Implementation of HttpRequestHandler for Aleatoric framework.
*  <br>
 * Converts HTTP requests to AleatoricCommandRequest and delegates to the AI model
 * via AleatoricCopilotGateway.
 */
@Component
public class AleatoricHttpRequestHandler implements HttpRequestHandler {

    private final AleatoricCopilotGateway gateway;

    public AleatoricHttpRequestHandler(AleatoricCopilotGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ResponseEntity<?> handle(HttpRequestContext context, String instructions) {
        try {
            // Build request for Aleatoric gateway
            AleatoricCommandRequest commandRequest = new AleatoricCommandRequest(
                    context.method() + " " + context.path(),
                    instructions,
                    Map.of(
                            "method", context.method(),
                            "path", context.path(),
                            "queryParams", context.queryParams(),
                            "headers", context.headers(),
                            "body", context.body()
                    ),
                    Map.of(
                            "sessionId", UUID.randomUUID().toString(),
                            "timestamp", System.currentTimeMillis()
                    )
            );

            // Execute through Aleatoric gateway
            AleatoricCommandResponse response = gateway.execute(commandRequest);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "error", "Aleatoric chain execution failed",
                            "message", e.getMessage()
                    ));
        }
    }

}

