package io.github.scitia.domain.quote.api.response;

import java.math.BigDecimal;

public record QuotePricingResponse(
        BigDecimal grossAmount,
        BigDecimal discountRate,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {
}
