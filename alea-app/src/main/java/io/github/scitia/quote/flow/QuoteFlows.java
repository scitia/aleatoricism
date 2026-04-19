package io.github.scitia.quote.flow;

import static lombok.AccessLevel.PRIVATE;

import io.github.scitia.alea.core.annotation.BusinessFlow;
import io.github.scitia.alea.core.engine.BusinessFlowBuilder;
import io.github.scitia.alea.core.engine.FlowDefinition;
import io.github.scitia.quote.api.QuoteRequest;
import io.github.scitia.quote.domain.QuoteDecision;
import io.github.scitia.quote.flow.process.decision.AssessRiskWaypoint;
import io.github.scitia.quote.flow.process.emission.PublishDecisionAuditWaypoint;
import io.github.scitia.quote.flow.process.transform.CalculatePricingWaypoint;
import io.github.scitia.quote.flow.process.transform.NormalizeQuoteInputWaypoint;
import io.github.scitia.quote.flow.process.value.BuildQuoteDecisionWaypoint;
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
}
