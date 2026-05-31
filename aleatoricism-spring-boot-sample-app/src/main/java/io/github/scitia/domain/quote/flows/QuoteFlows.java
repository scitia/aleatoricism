package io.github.scitia.domain.quote.flows;

import static lombok.AccessLevel.PRIVATE;

import io.github.scitia.aleatoricism.flows.annotation.BusinessFlow;
import io.github.scitia.aleatoricism.flows.engine.BusinessFlowBuilder;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.domain.quote.api.request.QuoteRequest;
import io.github.scitia.domain.quote.domain.PricingResult;
import io.github.scitia.domain.quote.domain.QuoteDecision;
import io.github.scitia.domain.quote.domain.RiskAssessment;
import io.github.scitia.domain.quote.flows.process.decision.AssessRiskWaypoint;
import io.github.scitia.domain.quote.flows.process.emission.PublishDecisionAuditWaypoint;
import io.github.scitia.domain.quote.flows.process.transform.CalculatePricingWaypoint;
import io.github.scitia.domain.quote.flows.process.transform.NormalizeQuoteInputWaypoint;
import io.github.scitia.domain.quote.flows.process.value.BuildQuoteDecisionWaypoint;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class QuoteFlows {

        @BusinessFlow(
                name = "quote-evaluation-flow", 
                description = """
                        Normalizes quote input, evaluates pricing and risk in parallel,
                        builds the final decision, and publishes an audit event.
                """           
        )
        public static final Flow<QuoteRequest, QuoteDecision> QUOTE_EVALUATION_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .parallel(CalculatePricingWaypoint.INSTANCE, AssessRiskWaypoint.INSTANCE)
                        .then(BuildQuoteDecisionWaypoint.INSTANCE)
                        .emit(PublishDecisionAuditWaypoint.INSTANCE)
                        .build();

        @BusinessFlow(
                name = "quote-pricing-flow",
                description = "Normalizes quote input and calculates pricing totals without risk assessment or final decision building."
        )
        public static final Flow<QuoteRequest, PricingResult> QUOTE_PRICING_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .then(CalculatePricingWaypoint.INSTANCE)
                        .build();

        @BusinessFlow(
                name = "quote-risk-flow",
                description = "Normalizes quote input and assesses risk with acceptance eligibility, without calculating pricing totals."
        )
        public static final Flow<QuoteRequest, RiskAssessment> QUOTE_RISK_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .then(AssessRiskWaypoint.INSTANCE)
                        .build();
}
