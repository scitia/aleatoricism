package io.github.scitia.quote.flows.process.emission;

import io.github.scitia.quote.domain.QuoteDecision;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNull;

class PublishDecisionAuditWaypointTest {

    @Test
    void shouldReturnNullAndNotThrowWhenPublishingAudit() {
        // given
        QuoteDecision decision = new QuoteDecision(
                "qd-test",
                new BigDecimal("1188.00"),
                new BigDecimal("225.72"),
                new BigDecimal("1413.72"),
                "LOW",
                true
        );

        // when
        Void result = PublishDecisionAuditWaypoint.INSTANCE.handle(decision, null);

        // then
        assertNull(result);
    }
}
