package io.github.scitia.app.sprint.flow;

import io.github.scitia.aleatoricism.flows.engine.BusinessFlowBuilder;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.app.sprint.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.Sprint;
import io.github.scitia.app.sprint.flow.plan.AddIssuesToSprintWaypoint;
import io.github.scitia.app.sprint.flow.plan.CreateSprintEntityWaypoint;
import io.github.scitia.app.sprint.flow.plan.PersistSprintWaypoint;
import io.github.scitia.app.sprint.flow.plan.ValidateSprintPlanningRequestWaypoint;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SprintFlows {

    private final PersistSprintWaypoint persistSprintWaypoint;

    @Setter
    @Getter
    public static class ExampleStore {
        private SprintPlanningRequest sprintPlanningRequest;
    }

    @Bean(name = "sprint-planning-flow")
    public Flow<SprintPlanningRequest, Sprint, ?> sprintPlanningFlow() {
        return BusinessFlowBuilder.define()
                .start(new ValidateSprintPlanningRequestWaypoint(), ExampleStore::new)
                .then(new CreateSprintEntityWaypoint())
                .then(new AddIssuesToSprintWaypoint())
                .then(persistSprintWaypoint)
                .build();
    }
}
