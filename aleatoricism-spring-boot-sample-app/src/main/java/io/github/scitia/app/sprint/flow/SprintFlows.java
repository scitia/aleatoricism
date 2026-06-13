package io.github.scitia.app.sprint.flow;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.engine.BusinessFlowBuilder;
import io.github.scitia.aleatoricism.flows.engine.Flow;
import io.github.scitia.app.sprint.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.Sprint;
import io.github.scitia.app.sprint.flow.plan.AddIssuesToSprintWaypoint;
import io.github.scitia.app.sprint.flow.plan.CreateSprintEntityWaypoint;
import io.github.scitia.app.sprint.flow.plan.ValidateSprintPlanningRequestWaypoint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

@Configuration
public class SprintFlows {

    @Setter
    @Getter
    public static class ExampleStore {
        private SprintPlanningRequest sprintPlanningRequest;
    }

    @Bean(name = "sprint-planning-flow")
    public Flow<SprintPlanningRequest, Sprint, ExampleStore> sprintPlanningFlow() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return BusinessFlowBuilder.define()
                .start(new ValidateSprintPlanningRequestWaypoint())
                .then(new CreateSprintEntityWaypoint(), (Consumer<ExampleStore>) store -> {
                    store.setSprintPlanningRequest(store.getSprintPlanningRequest());
                })
                .then(new AddIssuesToSprintWaypoint())
                .then((Waypoint<Sprint, Sprint>) (sprint, context) -> sprint)
                .withStore(ExampleStore.class);
    }
}
