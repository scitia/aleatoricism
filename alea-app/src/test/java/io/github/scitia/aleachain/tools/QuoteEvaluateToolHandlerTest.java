package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.alea.core.execution.ExecutionOptions;
import io.github.scitia.app.quote.api.QuoteResponse;
import io.github.scitia.config.aleatoric.tools.QuoteEvaluateToolHandler;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuoteEvaluateToolHandlerTest {

    @Test
    void shouldEvaluateQuoteByToolInvocation() {
        // given
        try (FlowEngine flowEngine = new FlowEngine(ExecutionOptions.waitForAll())) {
            QuoteEvaluateToolHandler handler = new QuoteEvaluateToolHandler(flowEngine);
            ToolInvocation invocation = new ToolInvocation()
                    .setToolName("quote_evaluate")
                    .setArguments(new ObjectMapper().valueToTree(Map.of(
                            "customerId", "cust-42",
                            "units", 12,
                            "unitPrice", 110.0,
                            "premiumCustomer", true,
                            "expedited", false
                    )));

            // when
            Object result = handler.invoke(invocation).join();

            // then
            QuoteResponse response = (QuoteResponse) result;
            assertNotNull(response.decisionId());
            assertTrue(response.accepted());
            assertEquals("LOW", response.riskLevel());
        }
    }
}
