package io.github.scitia.app.agile.api.response;

import java.util.UUID;

public record SprintSummaryResponse(
        UUID sprintId,
        String name,
        String goal,
        String startDate,
        String endDate,
        int capacity,
        String team,
        String status
) {
}

