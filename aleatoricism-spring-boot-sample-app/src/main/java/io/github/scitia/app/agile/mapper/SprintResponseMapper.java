package io.github.scitia.app.agile.mapper;

import io.github.scitia.app.agile.api.response.SprintSummaryResponse;
import io.github.scitia.app.agile.domain.sprint.Sprint;

import java.util.function.Function;

public enum SprintResponseMapper implements Function<Sprint, SprintSummaryResponse> {

    INSTANCE;

    @Override
    public SprintSummaryResponse apply(Sprint sprint) {
        return new SprintSummaryResponse(
                sprint.getSprintId(),
                sprint.getName(),
                sprint.getGoal(),
                sprint.getStartDate(),
                sprint.getEndDate(),
                sprint.getCapacity(),
                sprint.getTeam(),
                sprint.getStatus()
        );
    }
}

