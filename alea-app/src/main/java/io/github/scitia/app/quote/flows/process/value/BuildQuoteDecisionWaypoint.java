package io.github.scitia.app.quote.flows.process.value;

import io.github.scitia.alea.core.annotation.WaypointContract;
import io.github.scitia.alea.core.annotation.WaypointContractType;
import io.github.scitia.alea.core.api.Waypoint;
import io.github.scitia.alea.core.execution.ExecutionContext;
import io.github.scitia.app.quote.domain.PricingResult;
import io.github.scitia.app.quote.domain.QuoteDecision;
import io.github.scitia.app.quote.domain.RiskAssessment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@WaypointContract(
        name = "Build Decision",
        description = "Merges pricing and risk branch results into final closed output",
        contractType = WaypointContractType.CLOSED_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuildQuoteDecisionWaypoint implements Waypoint<Map.Entry<PricingResult, RiskAssessment>, QuoteDecision> {

    public static final BuildQuoteDecisionWaypoint INSTANCE = new BuildQuoteDecisionWaypoint();

    @Override
    public QuoteDecision handle(Map.Entry<PricingResult, RiskAssessment> input, ExecutionContext context) {
        PricingResult pricing = input.getKey();
        RiskAssessment risk = input.getValue();

        String decisionId = "qd-" + Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
        return new QuoteDecision(
                decisionId,
                pricing.netAmount(),
                pricing.taxAmount(),
                pricing.totalAmount(),
                risk.riskLevel(),
                risk.accepted()
        );
    }
}
