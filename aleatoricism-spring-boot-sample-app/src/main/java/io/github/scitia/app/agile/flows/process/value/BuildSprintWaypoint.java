package io.github.scitia.app.agile.flows.process.value;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.api.request.SprintCreateRequest;
import io.github.scitia.app.agile.domain.sprint.Sprint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@WaypointContract(
        name = "Build Sprint",
        description = "Creates sprint summary with lifecycle metadata",
        contractType = WaypointContractType.CLOSED_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuildSprintWaypoint implements Waypoint<SprintCreateRequest, Sprint> {

    public static final BuildSprintWaypoint INSTANCE = new BuildSprintWaypoint();

    @Override
    public Sprint handle(SprintCreateRequest input, ExecutionContext context) {
        return new Sprint(
                UUID.randomUUID(),
                input.name(),
                input.goal(),
                input.startDate(),
                input.endDate(),
                input.capacity(),
                input.team(),
                "PLANNED"
        );
    }
}

