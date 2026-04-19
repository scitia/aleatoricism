package io.github.scitia.app.quote.mapper;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.Function;

import io.github.scitia.app.quote.api.response.QuoteRiskResponse;
import io.github.scitia.app.quote.domain.RiskAssessment;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class QuoteRiskResponseMapper implements Function<RiskAssessment, QuoteRiskResponse> {

    public static final QuoteRiskResponseMapper INSTANCE = new QuoteRiskResponseMapper();

    @Override
    public QuoteRiskResponse apply(RiskAssessment riskAssessment) {
        return new QuoteRiskResponse(
                riskAssessment.riskLevel(),
                riskAssessment.accepted()
        );
    }
}
