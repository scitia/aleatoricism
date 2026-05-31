package io.github.scitia.domain.quote.api.request;

public record QuoteRequest(
        String customerId,
        int units,
        double unitPrice,
        boolean premiumCustomer,
        boolean expedited
) {
}
