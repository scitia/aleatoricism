package io.github.scitia.aleatoricism.flows.execution;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DefaultExecutionContext<S> implements ExecutionContext<S> {

    private final S store;
    private final ExecutionOptions options;
    private final ExecutorService executor;

    private DefaultExecutionContext(ExecutionOptions options, S store) {
        this.options = options;
        this.executor = Executors.newCachedThreadPool();
        this.store = store;
    }

    public static <S> DefaultExecutionContext<S> create(S store) {
        return new DefaultExecutionContext<>(ExecutionOptions.defaults(), store);
    }

    public static <S> DefaultExecutionContext<S> create(ExecutionOptions options, S store) {
        return new DefaultExecutionContext<>(options, store);
    }

    @Override
    public ExecutionOptions options() {
        return options;
    }

    @Override
    public Executor executor() {
        return executor;
    }

    @Override
    public S getStore() {
        return store;
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
