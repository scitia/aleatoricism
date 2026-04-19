package io.github.scitia.quote.flows.process.transform;

import io.github.scitia.alea.core.annotation.WaypointContract;
import io.github.scitia.alea.core.annotation.WaypointContractType;
import io.github.scitia.alea.core.api.Waypoint;
import io.github.scitia.alea.core.execution.ExecutionContext;
import io.github.scitia.quote.api.QuoteRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@WaypointContract(
        name = "Normalize Quote Input",
        description = "Sanitizes incoming request and prepares canonical input for downstream waypoints",
        contractType = WaypointContractType.TRANSFORM
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NormalizeQuoteInputWaypoint implements Waypoint<QuoteRequest, QuoteRequest> {

    public static final NormalizeQuoteInputWaypoint INSTANCE = new NormalizeQuoteInputWaypoint();

    @Override
    public QuoteRequest handle(QuoteRequest input, ExecutionContext context) {
        String customerId = input.customerId() == null ? "anonymous" : input.customerId().trim().toLowerCase();
        int units = Math.max(1, input.units());
        double unitPrice = Math.max(0.0d, input.unitPrice());
        return new QuoteRequest(customerId, units, unitPrice, input.premiumCustomer(), input.expedited());
    }
}
