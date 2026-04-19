package io.github.scitia.config.aleatoric.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolHandler;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.app.quote.api.request.QuoteRequest;
import io.github.scitia.app.quote.flows.QuoteFlows;
import io.github.scitia.app.quote.mapper.QuoteRiskResponseMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public final class QuoteRiskAssessmentToolHandler implements ToolHandler, AgenticBusinessTool {

    private final FlowEngine flowEngine;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuoteRiskAssessmentToolHandler(FlowEngine flowEngine) {
        this.flowEngine = Objects.requireNonNull(flowEngine, "flowEngine cannot be null");
    }

    @Override
    public String toolName() {
        return "quote_risk_assessment";
    }

    @Override
    public String description() {
        return "Assess quote risk level and acceptance without pricing details";
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
                throw new IllegalArgumentException("Tool 'quote_risk_assessment' requires arguments payload");
            }

            QuoteRequest request = objectMapper.convertValue(arguments, QuoteRequest.class);
            return QuoteRiskResponseMapper.INSTANCE.apply(flowEngine.run(QuoteFlows.QUOTE_RISK_FLOW, request));
        });
    }
}
