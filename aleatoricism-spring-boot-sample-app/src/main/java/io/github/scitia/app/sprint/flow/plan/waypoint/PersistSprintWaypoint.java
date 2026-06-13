package io.github.scitia.app.sprint.flow.plan.waypoint;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.domain.sprint.Sprint;
import io.github.scitia.app.sprint.flow.plan.store.SprintPlanningStore;
import io.github.scitia.app.sprint.domain.sprint.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistSprintWaypoint implements Waypoint<Sprint, Sprint, SprintPlanningStore> {

    private final SprintRepository sprintRepository;

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext<SprintPlanningStore> context) {
        return sprintRepository.save(sprint);
    }
}

