package io.github.scitia.aleatoricism.flows.way;

import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;

@FunctionalInterface
public interface ConsumerWithContext<T, S> {
    void accept(T value, ExecutionContext<S> context);
}
