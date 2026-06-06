package io.github.scitia.app.agile.api.request;

public record BacklogItemRequest(
        String title,
        String description,
        String type,
        int storyPoints,
        int priority,
        String reporter
) {
}

