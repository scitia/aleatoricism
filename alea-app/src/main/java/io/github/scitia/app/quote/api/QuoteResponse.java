package io.github.scitia.app.quote.api;

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
