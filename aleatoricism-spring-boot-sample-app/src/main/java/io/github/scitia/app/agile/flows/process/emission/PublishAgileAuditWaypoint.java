package io.github.scitia.app.agile.flows.process.emission;

import io.github.scitia.aleatoricism.flows.annotation.WaypointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.EmissionPoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;
import io.github.scitia.app.agile.domain.sprint.Sprint;
import io.github.scitia.app.agile.domain.sprintplanning.SprintPlanning;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.LogManager.getLogger;

@WaypointContract(
        name = "Publish Agile Audit",
        description = "Open output point that emits scrum audit events",
        contractType = WaypointContractType.OPEN_OUTPUT
)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public final class PublishAgileAuditWaypoint<T> implements EmissionPoint<T> {

    public static final PublishAgileAuditWaypoint<Sprint> SPRINT = new PublishAgileAuditWaypoint<>("SPRINT");
    public static final PublishAgileAuditWaypoint<SprintPlanning> SPRINT_PLANNING = new PublishAgileAuditWaypoint<>("SPRINT_PLANNING");
    public static final PublishAgileAuditWaypoint<BacklogItem> BACKLOG_ITEM = new PublishAgileAuditWaypoint<>("BACKLOG_ITEM");

    private static final Logger logger = getLogger(PublishAgileAuditWaypoint.class);

    private final String eventType;

    private PublishAgileAuditWaypoint(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public Void handle(T input, ExecutionContext context) {
        logger.info("AGILE AUDIT | type={} payload={}", eventType, input);
        return null;
    }
}

