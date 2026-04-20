package io.github.scitia.quote.api.response;

public record QuoteRiskResponse(
        String riskLevel,
        boolean accepted
) {
}
