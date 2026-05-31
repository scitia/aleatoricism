package io.github.scitia.domain.quote.domain;

import java.math.BigDecimal;

import io.github.scitia.domain.quote.api.request.QuoteRequest;

public record PricingResult(
        QuoteRequest request,
        BigDecimal grossAmount,
        BigDecimal discountRate,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
}
