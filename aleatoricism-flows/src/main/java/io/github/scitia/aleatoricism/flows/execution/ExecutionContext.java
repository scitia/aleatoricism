package io.github.scitia.aleatoricism.flows.execution;

import java.util.concurrent.Executor;

public interface ExecutionContext extends AutoCloseable {

    ExecutionOptions options();

    Executor executor();

    @Override
    default void close() {
        // no-op by default
    }
}
