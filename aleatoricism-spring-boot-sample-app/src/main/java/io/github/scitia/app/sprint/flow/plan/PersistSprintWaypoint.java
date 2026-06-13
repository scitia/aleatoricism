package io.github.scitia.app.sprint.flow.plan;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.sprint.domain.Sprint;
import io.github.scitia.app.sprint.flow.SprintFlows;
import io.github.scitia.app.sprint.infrastructure.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistSprintWaypoint implements Waypoint<Sprint, Sprint, SprintFlows.ExampleStore> {

    private final SprintRepository sprintRepository;

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext<SprintFlows.ExampleStore> context) {
        return sprintRepository.save(sprint);
    }
}

