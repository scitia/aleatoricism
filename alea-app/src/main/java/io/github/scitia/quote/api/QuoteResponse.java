package io.github.scitia.quote.api;

import java.math.BigDecimal;

public record QuoteResponse(
        String decisionId,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        String riskLevel,
        boolean accepted
) {
}
