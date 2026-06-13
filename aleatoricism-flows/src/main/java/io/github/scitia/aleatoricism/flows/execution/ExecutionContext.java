package io.github.scitia.aleatoricism.flows.execution;

import java.util.concurrent.Executor;

public interface ExecutionContext<S>  extends AutoCloseable {

    ExecutionOptions options();

    Executor executor();

    S getStore();

    @Override
    default void close() {
        // no-op by default
    }
}
