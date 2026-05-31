package io.github.scitia.domain.quote.domain;

import java.math.BigDecimal;

public record QuoteDecision(
        String decisionId,
        BigDecimal netAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        String riskLevel,
        boolean accepted
) {
}
