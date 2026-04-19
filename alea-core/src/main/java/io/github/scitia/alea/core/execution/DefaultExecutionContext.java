package io.github.scitia.alea.core.execution;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DefaultExecutionContext implements ExecutionContext {

    private final ExecutionOptions options;
    private final ExecutorService executor;

    private DefaultExecutionContext(ExecutionOptions options) {
        this.options = options;
        this.executor = Executors.newCachedThreadPool();
    }

    public static DefaultExecutionContext create() {
        return new DefaultExecutionContext(ExecutionOptions.defaults());
    }

    public static DefaultExecutionContext create(ExecutionOptions options) {
        return new DefaultExecutionContext(options);
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
    public void close() {
        executor.shutdown();
    }
}
