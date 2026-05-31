package io.github.scitia.domain.quote.mapper;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.Function;

import io.github.scitia.domain.quote.api.response.QuotePricingResponse;
import io.github.scitia.domain.quote.domain.PricingResult;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class QuotePricingResponseMapper implements Function<PricingResult, QuotePricingResponse> {

    public static final QuotePricingResponseMapper INSTANCE = new QuotePricingResponseMapper();

    @Override
    public QuotePricingResponse apply(PricingResult pricingResult) {
        return new QuotePricingResponse(
                pricingResult.grossAmount(),
                pricingResult.discountRate(),
                pricingResult.netAmount(),
                pricingResult.taxAmount(),
                pricingResult.totalAmount()
        );
    }
}
