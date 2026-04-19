package io.github.scitia.app.quote.flows;

import static lombok.AccessLevel.PRIVATE;

import io.github.scitia.alea.core.annotation.BusinessFlow;
import io.github.scitia.alea.core.engine.BusinessFlowBuilder;
import io.github.scitia.alea.core.engine.FlowDefinition;
import io.github.scitia.app.quote.api.request.QuoteRequest;
import io.github.scitia.app.quote.domain.PricingResult;
import io.github.scitia.app.quote.domain.QuoteDecision;
import io.github.scitia.app.quote.domain.RiskAssessment;
import io.github.scitia.app.quote.flows.process.decision.AssessRiskWaypoint;
import io.github.scitia.app.quote.flows.process.emission.PublishDecisionAuditWaypoint;
import io.github.scitia.app.quote.flows.process.transform.CalculatePricingWaypoint;
import io.github.scitia.app.quote.flows.process.transform.NormalizeQuoteInputWaypoint;
import io.github.scitia.app.quote.flows.process.value.BuildQuoteDecisionWaypoint;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class QuoteFlows {

        @BusinessFlow(
                name = "quote-evaluation-flow", 
                description = """
                        Business graph decomposed to typed Way 
                        segments with parallel pricing and risk branches
                """           
        )
        public static final FlowDefinition<QuoteRequest, QuoteDecision> QUOTE_EVALUATION_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .parallel(CalculatePricingWaypoint.INSTANCE, AssessRiskWaypoint.INSTANCE)
                        .then(BuildQuoteDecisionWaypoint.INSTANCE)
                        .emit(PublishDecisionAuditWaypoint.INSTANCE)
                        .build();

        @BusinessFlow(
                name = "quote-pricing-flow",
                description = "Calculates pricing totals without final risk decision phase"
        )
        public static final FlowDefinition<QuoteRequest, PricingResult> QUOTE_PRICING_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .then(CalculatePricingWaypoint.INSTANCE)
                        .build();

        @BusinessFlow(
                name = "quote-risk-flow",
                description = "Assesses risk and acceptance without pricing totals"
        )
        public static final FlowDefinition<QuoteRequest, RiskAssessment> QUOTE_RISK_FLOW = BusinessFlowBuilder.define()
                        .start(NormalizeQuoteInputWaypoint.INSTANCE)
                        .then(AssessRiskWaypoint.INSTANCE)
                        .build();
}
