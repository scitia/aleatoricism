package io.github.scitia.app.agile.api.response;

import java.util.UUID;

public record BacklogItemResponse(
        UUID itemId,
        String title,
        String type,
        int storyPoints,
        int priority,
        String status
) {
}

