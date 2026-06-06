package io.github.scitia.app.agile.flows.process.value;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.api.request.BacklogItemRequest;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@WaypointContract(
        name = "Build Backlog Item",
        description = "Creates backlog item with identifier and lifecycle status",
        contractType = WaypointContractType.CLOSED_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BuildBacklogItemWaypoint implements Waypoint<BacklogItemRequest, BacklogItem> {

    public static final BuildBacklogItemWaypoint INSTANCE = new BuildBacklogItemWaypoint();

    @Override
    public BacklogItem handle(BacklogItemRequest input, ExecutionContext context) {
        return new BacklogItem(
                UUID.randomUUID(),
                input.title(),
                input.description(),
                input.type(),
                input.storyPoints(),
                input.priority(),
                input.reporter(),
                "NEW"
        );
    }
}

