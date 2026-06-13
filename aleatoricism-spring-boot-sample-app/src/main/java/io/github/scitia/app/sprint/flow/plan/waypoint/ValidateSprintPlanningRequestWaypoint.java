package io.github.scitia.app.sprint.flow.plan.waypoint;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ValidateSprintPlanningRequestWaypoint implements Waypoint<SprintPlanningRequest, SprintPlanningRequest, SprintPlanningStore> {

    @Override
    public SprintPlanningRequest handle(SprintPlanningRequest input, ExecutionContext<SprintPlanningStore> context) {
        context.getStore().setSprintPlanningRequest(input);
        return input;
    }
}

