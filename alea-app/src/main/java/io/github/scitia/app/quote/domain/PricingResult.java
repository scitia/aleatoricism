package io.github.scitia.app.quote.domain;

import java.math.BigDecimal;

import io.github.scitia.app.quote.api.QuoteRequest;

public record PricingResult(
        QuoteRequest request,
        BigDecimal grossAmount,
        BigDecimal discountRate,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
}
