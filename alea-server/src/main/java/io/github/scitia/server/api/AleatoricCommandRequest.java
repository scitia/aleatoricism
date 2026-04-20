package io.github.scitia.server.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record AleatoricCommandRequest(
        String intent,
        String instructions,
        @NotNull(message = "payload cannot be null")
        @Size(min = 1, message = "payload cannot be empty")
        Map<String, Object> payload,
        @NotNull(message = "metadata cannot be null")
        Map<String, Object> metadata
) {
}
