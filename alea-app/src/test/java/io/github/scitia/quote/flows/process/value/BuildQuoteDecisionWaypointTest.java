package io.github.scitia.quote.flows.process.value;

import org.junit.jupiter.api.Test;

import io.github.scitia.app.quote.api.request.QuoteRequest;
import io.github.scitia.app.quote.domain.PricingResult;
import io.github.scitia.app.quote.domain.QuoteDecision;
import io.github.scitia.app.quote.domain.RiskAssessment;
import io.github.scitia.app.quote.flows.process.value.BuildQuoteDecisionWaypoint;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BuildQuoteDecisionWaypointTest {

    @Test
    void shouldMergePricingAndRiskIntoDecision() {
        // given
        PricingResult pricing = new PricingResult(
                new QuoteRequest("cust-42", 12, 110.0, true, false),
                new BigDecimal("1320.00"),
                new BigDecimal("0.10"),
                new BigDecimal("1188.00"),
                new BigDecimal("225.72"),
                new BigDecimal("1413.72")
        );
        RiskAssessment risk = new RiskAssessment("HIGH", false);

        // when
        QuoteDecision decision = BuildQuoteDecisionWaypoint.INSTANCE.handle(Map.entry(pricing, risk), null);

        // then
        assertNotNull(decision.decisionId());
        assertEquals(0, decision.netAmount().compareTo(new BigDecimal("1188.00")));
        assertEquals(0, decision.taxAmount().compareTo(new BigDecimal("225.72")));
        assertEquals(0, decision.totalAmount().compareTo(new BigDecimal("1413.72")));
        assertEquals("HIGH", decision.riskLevel());
        assertFalse(decision.accepted());
    }
}
