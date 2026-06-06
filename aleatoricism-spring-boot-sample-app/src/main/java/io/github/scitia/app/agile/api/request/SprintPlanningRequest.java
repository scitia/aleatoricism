package io.github.scitia.app.agile.api.request;

import java.util.List;
import java.util.UUID;

public record SprintPlanningRequest(
        UUID sprintId,
        List<String> backlogItemIds,
        int plannedStoryPoints,
        int capacity,
        String team
) {
}

