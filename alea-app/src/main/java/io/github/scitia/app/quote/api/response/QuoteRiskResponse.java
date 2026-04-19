package io.github.scitia.app.quote.api.response;

public record QuoteRiskResponse(
        String riskLevel,
        boolean accepted
) {
}
