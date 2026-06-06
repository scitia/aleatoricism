package io.github.scitia.app.agile.flows.process.datapoint;

import io.github.scitia.aleatoricism.flows.annotation.DatapointContract;
import io.github.scitia.aleatoricism.flows.annotation.WaypointContractType;
import io.github.scitia.aleatoricism.flows.api.Datapoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItemRepository;
import org.springframework.stereotype.Component;

/**
 * Datapoint example demonstrating dependency injection of a repository.
 * Unlike Waypoint which uses Singleton pattern, Datapoint is a Spring-managed bean
 * that can have dependencies injected through the constructor.
 * <br>
 * This datapoint persists a backlog item to the repository after it's been created.
 */
@DatapointContract(
        name = "Persist Backlog Item",
        description = "Saves the backlog item to repository with injected dependency",
        contractType = WaypointContractType.CLOSED_OUTPUT
)
@Component
public class PersistBacklogItemDatapoint implements Datapoint<BacklogItem, BacklogItem> {

    private final BacklogItemRepository backlogItemRepository;

    public PersistBacklogItemDatapoint(BacklogItemRepository backlogItemRepository) {
        this.backlogItemRepository = backlogItemRepository;
    }

    @Override
    public BacklogItem handle(BacklogItem backlogItem, ExecutionContext context) {
        return backlogItemRepository.save(backlogItem);
    }
}

