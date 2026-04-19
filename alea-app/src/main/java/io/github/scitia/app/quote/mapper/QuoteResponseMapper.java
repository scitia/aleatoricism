package io.github.scitia.app.quote.mapper;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.Function;

import io.github.scitia.app.quote.api.QuoteResponse;
import io.github.scitia.app.quote.domain.QuoteDecision;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class QuoteResponseMapper implements Function<QuoteDecision, QuoteResponse> {

    public static final QuoteResponseMapper INSTANCE = new QuoteResponseMapper();

    @Override
    public QuoteResponse apply(QuoteDecision quoteDecision) {
    return new QuoteResponse(
        quoteDecision.decisionId(),
        quoteDecision.netAmount(),
        quoteDecision.taxAmount(),
        quoteDecision.totalAmount(),
        quoteDecision.riskLevel(),
        quoteDecision.accepted()
    );
    }
}