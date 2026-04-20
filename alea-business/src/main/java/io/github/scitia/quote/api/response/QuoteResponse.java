package io.github.scitia.quote.api.response;

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
