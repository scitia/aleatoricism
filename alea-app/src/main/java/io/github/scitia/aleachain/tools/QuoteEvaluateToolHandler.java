package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.quote.api.QuoteRequest;
import io.github.scitia.quote.flows.QuoteFlows;
import io.github.scitia.quote.mapper.QuoteResponseMapper;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class QuoteEvaluateToolHandler implements ToolHandler {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper;

    public QuoteEvaluateToolHandler(FlowEngine flowEngine, ObjectMapper objectMapper) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
    }

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'quote_evaluate' requires arguments payload");
            }

            QuoteRequest request = objectMapper.convertValue(arguments, QuoteRequest.class);
            return QuoteResponseMapper.INSTANCE.apply(flowEngine.run(QuoteFlows.QUOTE_EVALUATION_FLOW, request));
        });
    }
}
