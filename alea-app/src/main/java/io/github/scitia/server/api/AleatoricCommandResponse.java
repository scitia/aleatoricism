package io.github.scitia.server.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AleatoricCommandResponse(
        @NotBlank(message = "sessionId cannot be blank")
        String sessionId,
        @NotNull(message = "processResult cannot be null")
        Object processResult
) {
}
