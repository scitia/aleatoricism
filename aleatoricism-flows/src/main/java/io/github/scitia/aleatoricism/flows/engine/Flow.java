package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.way.Way;

import java.util.Objects;

/**
 * Executable business flow wrapper used by application entry points.
 */
public record Flow<I, O, S>(Way<I, O> way, S store) {

    public Flow(Way<I, O> way, S store) {
        this.way = Objects.requireNonNull(way, "way cannot be null");
        this.store = Objects.requireNonNull(store, "store cannot be null");
    }
}
