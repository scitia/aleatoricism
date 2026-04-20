package quote.flows.process.transform;

import org.junit.jupiter.api.Test;

import io.github.scitia.quote.api.request.QuoteRequest;
import io.github.scitia.quote.domain.PricingResult;
import io.github.scitia.quote.flows.process.transform.CalculatePricingWaypoint;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatePricingWaypointTest {

    @Test
    void shouldCalculatePricingForPremiumNonExpeditedRequest() {
        // given
        QuoteRequest request = new QuoteRequest("cust-42", 12, 110.0, true, false);

        // when
        PricingResult result = CalculatePricingWaypoint.INSTANCE.handle(request, null);

        // then
        assertEquals(0, result.grossAmount().compareTo(new BigDecimal("1320.00")));
        assertEquals(0, result.discountRate().compareTo(new BigDecimal("0.10")));
        assertEquals(0, result.netAmount().compareTo(new BigDecimal("1188.00")));
        assertEquals(0, result.taxAmount().compareTo(new BigDecimal("225.72")));
        assertEquals(0, result.totalAmount().compareTo(new BigDecimal("1413.72")));
    }

    @Test
    void shouldUseHigherTaxForExpeditedRequest() {
        // given
        QuoteRequest request = new QuoteRequest("cust-42", 2, 100.0, false, true);

        // when
        PricingResult result = CalculatePricingWaypoint.INSTANCE.handle(request, null);

        // then
        assertEquals(0, result.grossAmount().compareTo(new BigDecimal("200.00")));
        assertEquals(0, result.discountRate().compareTo(BigDecimal.ZERO));
        assertEquals(0, result.netAmount().compareTo(new BigDecimal("200.00")));
        assertEquals(0, result.taxAmount().compareTo(new BigDecimal("46.00")));
        assertEquals(0, result.totalAmount().compareTo(new BigDecimal("246.00")));
    }
}
