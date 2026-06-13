package io.github.scitia.app.sprint.flow;

import io.github.scitia.aleatoricism.flows.annotation.BusinessFlow;
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
    private final ValidateSprintPlanningRequestWaypoint validationWaypoint;

    @Bean(name = "sprint-planning-flow")
    @BusinessFlow(
            name = "sprint-planning-flow",
            description = """
                Flow to plan a new sprint, including validation,
                creation, and persistence of the sprint and its associated issues.
            """
    )
    public Flow<SprintPlanningRequest, Sprint, SprintPlanningStore> sprintPlanningFlow() {
        return BusinessFlowBuilder.define()
                .start(validationWaypoint, SprintPlanningStore::new)
                .then(new CreateSprintEntityWaypoint())
                .then(new AddIssuesToSprintWaypoint())
                .then(persistSprintWaypoint)
                .build();
    }
}
