package io.github.scitia.app.agile.api.request;

public record SprintCreateRequest(
        String name,
        String goal,
        String startDate,
        String endDate,
        int capacity,
        String team
) {
}

