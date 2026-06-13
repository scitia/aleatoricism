package io.github.scitia.app.sprint.flow.plan.waypoint;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.sprint.Sprint;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;

public class CreateSprintEntityWaypoint implements Waypoint<SprintPlanningRequest, Sprint, SprintPlanningStore> {

    @Override
    public Sprint handle(SprintPlanningRequest input, ExecutionContext<SprintPlanningStore> context) {
        Sprint sprint = new Sprint();
        sprint.setName(input.name());
        sprint.setGoal(input.goal());
        return sprint;
    }
}

