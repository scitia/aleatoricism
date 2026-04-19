package io.github.scitia.quote.flow.process.decision;

import io.github.scitia.alea.core.annotation.WaypointContract;
import io.github.scitia.alea.core.annotation.WaypointContractType;
import io.github.scitia.alea.core.api.Waypoint;
import io.github.scitia.alea.core.execution.ExecutionContext;
import io.github.scitia.quote.api.QuoteRequest;
import io.github.scitia.quote.domain.RiskAssessment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@WaypointContract(
        name = "Assess Risk",
        description = "Assesses request risk and returns acceptance decision",
        contractType = WaypointContractType.DECISION
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssessRiskWaypoint implements Waypoint<QuoteRequest, RiskAssessment> {

    public static final AssessRiskWaypoint INSTANCE = new AssessRiskWaypoint();

    @Override
    public RiskAssessment handle(QuoteRequest input, ExecutionContext context) {
        boolean highAmount = input.units() * input.unitPrice() > 25_000;
        boolean suspiciousCustomer = input.customerId().startsWith("x-");

        if (highAmount || suspiciousCustomer) {
            return new RiskAssessment("HIGH", false);
        }
        if (input.expedited()) {
            return new RiskAssessment("MEDIUM", true);
        }
        return new RiskAssessment("LOW", true);
    }
}
