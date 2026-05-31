package io.github.scitia.domain.quote.flows.process.transform;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.domain.quote.api.request.QuoteRequest;
import io.github.scitia.domain.quote.domain.PricingResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@WaypointContract(
        name = "Calculate Pricing",
        description = "Calculates gross/net/tax totals for a quote",
        contractType = WaypointContractType.TRANSFORM
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CalculatePricingWaypoint implements Waypoint<QuoteRequest, PricingResult> {

    public static final CalculatePricingWaypoint INSTANCE = new CalculatePricingWaypoint();

    @Override
    public PricingResult handle(QuoteRequest input, ExecutionContext context) {
        BigDecimal units = BigDecimal.valueOf(input.units());
        BigDecimal unitPrice = BigDecimal.valueOf(input.unitPrice());
        BigDecimal gross = units.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        BigDecimal discountRate = input.premiumCustomer() ? new BigDecimal("0.10") : BigDecimal.ZERO;
        BigDecimal net = gross.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxRate = input.expedited() ? new BigDecimal("0.23") : new BigDecimal("0.19");
        BigDecimal tax = net.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = net.add(tax).setScale(2, RoundingMode.HALF_UP);

        return new PricingResult(input, gross, discountRate, net, tax, total);
    }
}
