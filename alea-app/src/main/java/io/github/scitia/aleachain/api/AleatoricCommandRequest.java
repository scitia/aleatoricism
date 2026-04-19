package io.github.scitia.aleachain.api;

import java.util.Map;

public record AleatoricCommandRequest(
        String intent,
        String instructions,
        Map<String, Object> payload,
        Map<String, Object> metadata
) {
}
