package io.github.scitia.app.agile.flows;

import io.github.scitia.aleatoricism.flows.annotation.BusinessFlow;
import io.github.scitia.aleatoricism.flows.engine.BusinessFlowBuilder;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.app.agile.api.request.BacklogItemRequest;
import io.github.scitia.app.agile.api.request.SprintCreateRequest;
import io.github.scitia.app.agile.api.request.SprintPlanningRequest;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;
import io.github.scitia.app.agile.domain.sprint.Sprint;
import io.github.scitia.app.agile.domain.sprintplanning.SprintPlanning;
import io.github.scitia.app.agile.flows.process.datapoint.PersistBacklogItemDatapoint;
import io.github.scitia.app.agile.flows.process.datapoint.PersistSprintDatapoint;
import io.github.scitia.app.agile.flows.process.emission.PublishAgileAuditWaypoint;
import io.github.scitia.app.agile.flows.process.transform.NormalizeBacklogItemWaypoint;
import io.github.scitia.app.agile.flows.process.transform.NormalizeSprintPlanningWaypoint;
import io.github.scitia.app.agile.flows.process.transform.NormalizeSprintRequestWaypoint;
import io.github.scitia.app.agile.flows.process.value.BuildBacklogItemWaypoint;
import io.github.scitia.app.agile.flows.process.value.BuildSprintPlanningWaypoint;
import io.github.scitia.app.agile.flows.process.value.BuildSprintWaypoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgileFlows {

    @BusinessFlow(
            name = "agile-sprint-create-flow",
            description = "Normalizes sprint input, builds sprint summary, and emits audit event."
    )
    public static final Flow<SprintCreateRequest, Sprint> SPRINT_CREATE_FLOW = BusinessFlowBuilder.define()
            .start(NormalizeSprintRequestWaypoint.INSTANCE)
            .then(BuildSprintWaypoint.INSTANCE)
            .emit(PublishAgileAuditWaypoint.SPRINT)
            .build();

    @BusinessFlow(
            name = "agile-sprint-planning-flow",
            description = "Normalizes planning input, commits backlog items, and emits audit event."
    )
    public static final Flow<SprintPlanningRequest, SprintPlanning> SPRINT_PLANNING_FLOW = BusinessFlowBuilder.define()
            .start(NormalizeSprintPlanningWaypoint.INSTANCE)
            .then(BuildSprintPlanningWaypoint.INSTANCE)
            .emit(PublishAgileAuditWaypoint.SPRINT_PLANNING)
            .build();

    @BusinessFlow(
            name = "agile-backlog-item-flow",
            description = "Normalizes backlog item input, creates item, and emits audit event."
    )
    public static final Flow<BacklogItemRequest, BacklogItem> BACKLOG_ITEM_FLOW = BusinessFlowBuilder.define()
            .start(NormalizeBacklogItemWaypoint.INSTANCE)
            .then(BuildBacklogItemWaypoint.INSTANCE)
            .emit(PublishAgileAuditWaypoint.BACKLOG_ITEM)
            .build();

    @Bean
    public Flow<BacklogItemRequest, BacklogItem> enhancedBacklogItemFlow(
            PersistBacklogItemDatapoint persistDatapoint) {
        return BusinessFlowBuilder.define()
                .start(NormalizeBacklogItemWaypoint.INSTANCE)
                .then(BuildBacklogItemWaypoint.INSTANCE)
                .then(persistDatapoint)
                .emit(PublishAgileAuditWaypoint.BACKLOG_ITEM)
                .build();
    }

    @Bean
    public Flow<SprintCreateRequest, Sprint> enhancedSprintCreateFlow(
            PersistSprintDatapoint persistDatapoint) {
        return BusinessFlowBuilder.define()
                .start(NormalizeSprintRequestWaypoint.INSTANCE)
                .then(BuildSprintWaypoint.INSTANCE)
                .then(persistDatapoint)
                .emit(PublishAgileAuditWaypoint.SPRINT)
                .build();
    }
}

