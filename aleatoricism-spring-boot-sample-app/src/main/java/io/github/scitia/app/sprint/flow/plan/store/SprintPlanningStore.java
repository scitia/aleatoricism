package io.github.scitia.app.sprint.flow.plan.store;

import io.github.scitia.app.sprint.flow.api.SprintPlanningRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SprintPlanningStore {
    private SprintPlanningRequest sprintPlanningRequest;
}
