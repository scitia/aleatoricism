package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.alea.core.execution.ExecutionOptions;
import io.github.scitia.config.aleatoric.tools.QuoteRiskAssessmentToolHandler;
import io.github.scitia.quote.api.response.QuoteRiskResponse;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QuoteRiskAssessmentToolHandlerTest {

    @Test
    void shouldAssessHighRiskByToolInvocation() {
        // given
        try (FlowEngine flowEngine = new FlowEngine(ExecutionOptions.waitForAll())) {
            QuoteRiskAssessmentToolHandler handler = new QuoteRiskAssessmentToolHandler(flowEngine);
            ToolInvocation invocation = new ToolInvocation()
                    .setToolName("quote_risk_assessment")
                    .setArguments(new ObjectMapper().valueToTree(Map.of(
                            "customerId", "x-risky",
                            "units", 400,
                            "unitPrice", 100.0,
                            "premiumCustomer", false,
                            "expedited", true
                    )));

            // when
            Object result = handler.invoke(invocation).join();

            // then
            QuoteRiskResponse response = (QuoteRiskResponse) result;
            assertEquals("HIGH", response.riskLevel());
            assertFalse(response.accepted());
        }
    }
}
