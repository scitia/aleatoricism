package quote.flows.process.decision;

import org.junit.jupiter.api.Test;

import io.github.scitia.quote.api.request.QuoteRequest;
import io.github.scitia.quote.domain.RiskAssessment;
import io.github.scitia.quote.flows.process.decision.AssessRiskWaypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssessRiskWaypointTest {

    @Test
    void shouldReturnHighRiskForSuspiciousCustomer() {
        // given
        QuoteRequest request = new QuoteRequest("x-42", 1, 10.0, false, false);

        // when
        RiskAssessment result = AssessRiskWaypoint.INSTANCE.handle(request, null);

        // then
        assertEquals("HIGH", result.riskLevel());
        assertFalse(result.accepted());
    }

    @Test
    void shouldReturnMediumRiskForExpeditedRequest() {
        // given
        QuoteRequest request = new QuoteRequest("cust-42", 1, 10.0, false, true);

        // when
        RiskAssessment result = AssessRiskWaypoint.INSTANCE.handle(request, null);

        // then
        assertEquals("MEDIUM", result.riskLevel());
        assertTrue(result.accepted());
    }

    @Test
    void shouldReturnLowRiskForStandardRequest() {
        // given
        QuoteRequest request = new QuoteRequest("cust-42", 1, 10.0, false, false);

        // when
        RiskAssessment result = AssessRiskWaypoint.INSTANCE.handle(request, null);

        // then
        assertEquals("LOW", result.riskLevel());
        assertTrue(result.accepted());
    }
}
