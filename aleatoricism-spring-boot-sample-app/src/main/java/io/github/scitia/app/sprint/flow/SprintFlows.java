package io.github.scitia.app.sprint.flow;

import io.github.scitia.aleatoricism.flows.engine.BusinessFlowBuilder;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.sprint.Sprint;
import io.github.scitia.app.sprint.flow.plan.waypoint.AddIssuesToSprintWaypoint;
import io.github.scitia.app.sprint.flow.plan.waypoint.CreateSprintEntityWaypoint;
import io.github.scitia.app.sprint.flow.plan.waypoint.PersistSprintWaypoint;
import io.github.scitia.app.sprint.flow.plan.waypoint.ValidateSprintPlanningRequestWaypoint;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SprintFlows {

    private final PersistSprintWaypoint persistSprintWaypoint;

    @Bean(name = "sprint-planning-flow")
    public Flow<SprintPlanningRequest, Sprint, SprintPlanningStore> sprintPlanningFlow() {
        return BusinessFlowBuilder.define()
                .start(new ValidateSprintPlanningRequestWaypoint(), SprintPlanningStore::new)
                .then(new CreateSprintEntityWaypoint())
                .then(new AddIssuesToSprintWaypoint())
                .then(persistSprintWaypoint)
                .build();
    }
}
