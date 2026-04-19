package io.github.scitia.quote;

import io.github.scitia.quote.api.QuoteRequest;
import io.github.scitia.quote.api.QuoteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QuoteControllerTest {

    @Autowired
  private QuoteController quoteController;

    @Test
    void shouldEvaluateQuoteThroughDecomposedBusinessGraph() throws Exception {
      QuoteRequest request = new QuoteRequest("cust-42", 12, 110.0, true, false);

      QuoteResponse response = quoteController.evaluate(request);

      assertNotNull(response.decisionId());
      assertTrue(response.accepted());
      assertEquals("LOW", response.riskLevel());
      assertEquals(0, response.netAmount().compareTo(new java.math.BigDecimal("1188.00")));
      assertEquals(0, response.taxAmount().compareTo(new java.math.BigDecimal("225.72")));
      assertEquals(0, response.totalAmount().compareTo(new java.math.BigDecimal("1413.72")));
    }

    @Test
    void shouldReturnNegativeDecisionForHighRiskInput() {
      QuoteRequest request = new QuoteRequest("x-42", 12, 110.0, true, false);

      QuoteResponse response = quoteController.evaluate(request);

      assertNotNull(response.decisionId());
      assertEquals("HIGH", response.riskLevel());
      assertEquals(false, response.accepted());
      assertEquals(0, response.netAmount().compareTo(new java.math.BigDecimal("1188.00")));
      assertEquals(0, response.taxAmount().compareTo(new java.math.BigDecimal("225.72")));
      assertEquals(0, response.totalAmount().compareTo(new java.math.BigDecimal("1413.72")));
    }
}
