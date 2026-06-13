package io.github.scitia.app.sprint.flow.plan;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.api.SprintPlanningRequest;
import io.github.scitia.app.sprint.domain.Sprint;

public class CreateSprintEntityWaypoint implements Waypoint<SprintPlanningRequest, Sprint> {

    @Override
    public Sprint handle(SprintPlanningRequest input, ExecutionContext context) throws Exception {
        Sprint sprint = new Sprint();
        sprint.setName(input.name());
        sprint.setGoal(input.goal());
        return sprint;
    }
}

