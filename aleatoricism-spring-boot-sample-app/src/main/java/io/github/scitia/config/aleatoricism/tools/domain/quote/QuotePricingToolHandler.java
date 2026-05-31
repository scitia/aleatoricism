package io.github.scitia.config.aleatoricism.tools.domain.quote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.config.aleatoricism.tools.AgenticBusinessTool;
import io.github.scitia.domain.quote.api.request.QuoteRequest;
import io.github.scitia.domain.quote.flows.QuoteFlows;
import io.github.scitia.domain.quote.mapper.QuotePricingResponseMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class QuotePricingToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuotePricingToolHandler(FlowEngine flowEngine) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
    }

    @Override
    public String toolName() {
        return "quote_pricing";
    }

    @Override
    public String description() {
        return "Calculate quote pricing totals without risk decision";
    }

    @Override
    public Map<String, Object> inputSchema() {
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

    @Override
    public CompletableFuture<Object> invoke(ToolInvocation invocation) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> arguments = invocation.getArguments();
            if (arguments == null || arguments.isEmpty()) {
                throw new IllegalArgumentException("Tool 'quote_pricing' requires arguments payload");
            }

            QuoteRequest request = objectMapper.convertValue(arguments, QuoteRequest.class);
            return QuotePricingResponseMapper.INSTANCE.apply(flowEngine.run(QuoteFlows.QUOTE_PRICING_FLOW, request));
        });
    }
}
