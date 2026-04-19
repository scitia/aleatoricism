package io.github.scitia.quote.domain;

import io.github.scitia.quote.api.QuoteRequest;

import java.math.BigDecimal;

public record PricingResult(
        QuoteRequest request,
        BigDecimal grossAmount,
        BigDecimal discountRate,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
}
