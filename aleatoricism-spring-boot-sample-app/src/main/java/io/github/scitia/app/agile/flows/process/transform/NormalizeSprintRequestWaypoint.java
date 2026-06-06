package io.github.scitia.app.agile.flows.process.transform;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.api.request.SprintCreateRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@WaypointContract(
        name = "Normalize Sprint Input",
        description = "Sanitizes sprint request fields and applies defaults",
        contractType = WaypointContractType.TRANSFORM
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NormalizeSprintRequestWaypoint implements Waypoint<SprintCreateRequest, SprintCreateRequest> {

    public static final NormalizeSprintRequestWaypoint INSTANCE = new NormalizeSprintRequestWaypoint();

    @Override
    public SprintCreateRequest handle(SprintCreateRequest input, ExecutionContext context) {
        String name = normalize(input.name(), "Sprint");
        String goal = normalize(input.goal(), "Deliver value");
        String startDate = normalize(input.startDate(), "TBD");
        String endDate = normalize(input.endDate(), "TBD");
        int capacity = Math.max(1, input.capacity());
        String team = normalize(input.team(), "core-team");
        return new SprintCreateRequest(name, goal, startDate, endDate, capacity, team);
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }
}

