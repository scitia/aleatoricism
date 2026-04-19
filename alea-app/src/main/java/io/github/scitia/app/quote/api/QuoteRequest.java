package io.github.scitia.app.quote.api;

public record QuoteRequest(
        String customerId,
        int units,
        double unitPrice,
        boolean premiumCustomer,
        boolean expedited
) {
}
