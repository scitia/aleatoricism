package io.github.scitia.aleachain.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.copilot.sdk.json.ToolInvocation;
import io.github.scitia.alea.core.engine.FlowEngine;
import io.github.scitia.alea.core.execution.ExecutionOptions;
import io.github.scitia.config.aleatoric.tools.QuotePricingToolHandler;
import io.github.scitia.quote.api.response.QuotePricingResponse;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuotePricingToolHandlerTest {

    @Test
    void shouldCalculateQuotePricingByToolInvocation() {
        // given
        try (FlowEngine flowEngine = new FlowEngine(ExecutionOptions.waitForAll())) {
            QuotePricingToolHandler handler = new QuotePricingToolHandler(flowEngine);
            ToolInvocation invocation = new ToolInvocation()
                    .setToolName("quote_pricing")
                    .setArguments(new ObjectMapper().valueToTree(Map.of(
                            "customerId", "cust-77",
                            "units", 10,
                            "unitPrice", 100.0,
                            "premiumCustomer", true,
                            "expedited", false
                    )));

            // when
            Object result = handler.invoke(invocation).join();

            // then
            QuotePricingResponse response = (QuotePricingResponse) result;
            assertEquals(new BigDecimal("1000.00"), response.grossAmount());
            assertEquals(new BigDecimal("0.10"), response.discountRate());
            assertEquals(new BigDecimal("900.00"), response.netAmount());
            assertEquals(new BigDecimal("171.00"), response.taxAmount());
            assertEquals(new BigDecimal("1071.00"), response.totalAmount());
        }
    }
}
