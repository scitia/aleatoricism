package io.github.scitia.quote.mapper;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.Function;

import io.github.scitia.quote.api.response.QuoteResponse;
import io.github.scitia.quote.domain.QuoteDecision;
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