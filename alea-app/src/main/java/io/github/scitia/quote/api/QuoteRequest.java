package io.github.scitia.quote.api;

public record QuoteRequest(
        String customerId,
        int units,
        double unitPrice,
        boolean premiumCustomer,
        boolean expedited
) {
}
