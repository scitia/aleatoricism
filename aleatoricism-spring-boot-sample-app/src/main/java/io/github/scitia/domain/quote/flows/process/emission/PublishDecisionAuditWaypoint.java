package io.github.scitia.domain.quote.flows.process.emission;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.EmissionPoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.domain.quote.domain.QuoteDecision;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@WaypointContract(
        name = "Publish Decision Audit",
        description = "Open output point that emits side-effect event to an external channel",
        contractType = WaypointContractType.OPEN_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublishDecisionAuditWaypoint implements EmissionPoint<QuoteDecision> {

    public static final PublishDecisionAuditWaypoint INSTANCE = new PublishDecisionAuditWaypoint();
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(PublishDecisionAuditWaypoint.class);

    @Override
    public Void handle(QuoteDecision input, ExecutionContext context) {
        logger.info("AUDIT EVENT | decisionId={} total={} accepted={} risk={}",
                input.decisionId(),
                input.totalAmount(),
                input.accepted(),
                input.riskLevel());
        return null;
    }
}
