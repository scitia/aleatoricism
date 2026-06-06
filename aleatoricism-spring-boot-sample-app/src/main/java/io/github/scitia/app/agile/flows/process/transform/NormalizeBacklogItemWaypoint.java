package io.github.scitia.app.agile.flows.process.transform;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.api.request.BacklogItemRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.Objects.isNull;

@WaypointContract(
        name = "Normalize Backlog Item",
        description = "Sanitizes backlog item request fields and sets defaults",
        contractType = WaypointContractType.TRANSFORM
)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NormalizeBacklogItemWaypoint implements Waypoint<BacklogItemRequest, BacklogItemRequest> {

    public static final NormalizeBacklogItemWaypoint INSTANCE = new NormalizeBacklogItemWaypoint();

    @Override
    public BacklogItemRequest handle(BacklogItemRequest input, ExecutionContext context) {
        String title = normalize(input.title(), "Untitled item");
        String description = input.description() == null ? "" : input.description().trim();
        String type = normalize(input.type(), "STORY").toUpperCase();
        int storyPoints = Math.max(1, input.storyPoints());
        int priority = clamp(input.priority());
        String reporter = normalize(input.reporter(), "anonymous");
        return new BacklogItemRequest(title, description, type, storyPoints, priority, reporter);
    }

    private String normalize(String value, String fallback) {
        if (isNull(value) || value.isBlank()) {
            return fallback;
        }
        return value.trim();
    }

    private int clamp(int value) {
        return Math.clamp(value, 1, 5);
    }
}

