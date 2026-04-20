package io.github.scitia.alea.core.engine;

import io.github.scitia.alea.core.way.Way;

import java.util.Objects;

/**
 * Executable business flow wrapper used by application entry points.
 */
public record Flow<I, O>(Way<I, O> way) {

    public Flow {
        Objects.requireNonNull(way, "way cannot be null");
    }
}
