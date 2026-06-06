package io.github.scitia.app.agile.mapper;

import io.github.scitia.app.agile.api.response.BacklogItemResponse;
import io.github.scitia.app.agile.domain.backlogitem.BacklogItem;

import java.util.function.Function;

public enum BacklogItemResponseMapper implements Function<BacklogItem, BacklogItemResponse> {

    INSTANCE;

    @Override
    public BacklogItemResponse apply(BacklogItem backlogItem) {
        return new BacklogItemResponse(
                backlogItem.getItemId(),
                backlogItem.getTitle(),
                backlogItem.getType(),
                backlogItem.getStoryPoints(),
                backlogItem.getPriority(),
                backlogItem.getStatus()
        );
    }
}

