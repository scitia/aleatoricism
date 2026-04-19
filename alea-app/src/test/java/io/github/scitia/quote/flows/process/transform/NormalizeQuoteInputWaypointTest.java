package io.github.scitia.quote.flows.process.transform;

import org.junit.jupiter.api.Test;

import io.github.scitia.app.quote.api.QuoteRequest;
import io.github.scitia.app.quote.flows.process.transform.NormalizeQuoteInputWaypoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NormalizeQuoteInputWaypointTest {

    @Test
    void shouldNormalizeAndSanitizeInput() {
        // given
        QuoteRequest input = new QuoteRequest("  CUST-42  ", 0, -12.5, true, false);

        // when
        QuoteRequest result = NormalizeQuoteInputWaypoint.INSTANCE.handle(input, null);

        // then
        assertEquals("cust-42", result.customerId());
        assertEquals(1, result.units());
        assertEquals(0.0d, result.unitPrice());
        assertTrue(result.premiumCustomer());
        assertFalse(result.expedited());
    }

    @Test
    void shouldUseAnonymousCustomerWhenIdIsNull() {
        // given
        QuoteRequest input = new QuoteRequest(null, 2, 10.0, false, true);

        // when
        QuoteRequest result = NormalizeQuoteInputWaypoint.INSTANCE.handle(input, null);

        // then
        assertEquals("anonymous", result.customerId());
        assertEquals(2, result.units());
        assertEquals(10.0d, result.unitPrice());
        assertFalse(result.premiumCustomer());
        assertTrue(result.expedited());
    }
}
