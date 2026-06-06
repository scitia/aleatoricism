package io.github.scitia.app.agile.api.response;

import java.util.List;
import java.util.UUID;

public record SprintPlanningResponse(
        UUID sprintId,
        List<String> committedItems,
        int plannedStoryPoints,
        int capacity,
        boolean overflow,
        String status
) {
}

