package io.github.scitia.app.agile.flows.process.datapoint;

import io.github.scitia.aleatoricism.flows.annotation.DatapointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Datapoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.domain.sprint.Sprint;
import io.github.scitia.app.agile.domain.sprint.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Datapoint example demonstrating dependency injection of a service.
 * <br>
 * This datapoint persists a sprint to the service after it's been created.
 * It demonstrates how Datapoint allows for clean separation of concerns by
 * injecting dependencies (SprintService) rather than using Singleton pattern.
 */
@DatapointContract(
        name = "Persist Sprint",
        description = "Saves the sprint to service with injected dependency",
        contractType = WaypointContractType.CLOSED_OUTPUT
)
@Component
@RequiredArgsConstructor
public class PersistSprintDatapoint implements Datapoint<Sprint, Sprint> {

    private final SprintRepository sprintRepository;

    @Override
    public Sprint handle(Sprint sprint, ExecutionContext context) {
        return sprintRepository.save(sprint);
    }
}

