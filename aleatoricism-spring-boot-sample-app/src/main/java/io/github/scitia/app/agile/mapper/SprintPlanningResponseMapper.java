package io.github.scitia.app.agile.mapper;

import io.github.scitia.app.agile.api.response.SprintPlanningResponse;
import io.github.scitia.app.agile.domain.sprintplanning.SprintPlanning;

import java.util.function.Function;

public enum SprintPlanningResponseMapper implements Function<SprintPlanning, SprintPlanningResponse> {

    INSTANCE;

    @Override
    public SprintPlanningResponse apply(SprintPlanning planning) {
        return new SprintPlanningResponse(
                planning.getSprintId(),
                planning.getCommittedItems(),
                planning.getPlannedStoryPoints(),
                planning.getCapacity(),
                planning.isOverflow(),
                planning.getStatus()
        );
    }
}

