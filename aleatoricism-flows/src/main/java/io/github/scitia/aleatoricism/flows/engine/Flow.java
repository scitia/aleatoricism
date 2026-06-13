package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.way.Way;

import java.util.Objects;

/**
 * Executable business flow wrapper used by application entry points.
 */
public class Flow<I, O, S> {

    private final Way<I, O, S> way;
    private final S store;

    public Flow(Way<I, O, S> way, S store) {
        this.way = Objects.requireNonNull(way, "way cannot be null");
        this.store = Objects.requireNonNull(store, "store cannot be null");
    }

    public Way<I, O, S> getWay() {
        return way;
    }

    public S getStore() {
        return store;
    }
}
