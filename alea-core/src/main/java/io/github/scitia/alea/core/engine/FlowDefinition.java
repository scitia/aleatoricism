package io.github.scitia.alea.core.engine;

import io.github.scitia.alea.core.way.Way;

import java.util.Objects;

/**
 * Executable business flow wrapper used by application entry points.
 */
public record FlowDefinition<I, O>(Way<I, O> way) {

    public FlowDefinition {
        Objects.requireNonNull(way, "way cannot be null");
    }
}
