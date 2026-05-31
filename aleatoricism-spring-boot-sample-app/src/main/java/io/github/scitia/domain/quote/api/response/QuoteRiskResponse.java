package io.github.scitia.domain.quote.api.response;

public record QuoteRiskResponse(
        String riskLevel,
        boolean accepted
) {
}
