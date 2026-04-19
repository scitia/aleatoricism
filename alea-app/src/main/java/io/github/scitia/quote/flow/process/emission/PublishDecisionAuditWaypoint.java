package io.github.scitia.quote.flow.process.emission;

import io.github.scitia.alea.core.annotation.WaypointContract;
import io.github.scitia.alea.core.annotation.WaypointContractType;
import io.github.scitia.alea.core.api.OpenOutputPoint;
import io.github.scitia.alea.core.execution.ExecutionContext;
import io.github.scitia.quote.domain.QuoteDecision;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WaypointContract(
        name = "Publish Decision Audit",
        description = "Open output point that emits side-effect event to an external channel",
        contractType = WaypointContractType.OPEN_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublishDecisionAuditWaypoint implements OpenOutputPoint<QuoteDecision> {

    public static final PublishDecisionAuditWaypoint INSTANCE = new PublishDecisionAuditWaypoint();

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishDecisionAuditWaypoint.class);

    @Override
    public Void handle(QuoteDecision input, ExecutionContext context) {
        LOGGER.info("AUDIT EVENT | decisionId={} total={} accepted={} risk={}",
                input.decisionId(),
                input.totalAmount(),
                input.accepted(),
                input.riskLevel());
        return null;
    }
}
