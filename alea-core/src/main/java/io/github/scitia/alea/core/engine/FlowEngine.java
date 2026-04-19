package io.github.scitia.alea.core.engine;

import io.github.scitia.alea.core.execution.DefaultExecutionContext;
import io.github.scitia.alea.core.execution.ExecutionContext;
import io.github.scitia.alea.core.execution.ExecutionOptions;
import io.github.scitia.alea.core.way.Way;

import java.util.Objects;

/**
 * Executes a business flow inside a shared execution context.
 */
public final class FlowEngine implements AutoCloseable {

    private final ExecutionContext context;
    private final boolean ownsContext;

    public FlowEngine() {
        this(DefaultExecutionContext.create(), true);
    }

    public FlowEngine(ExecutionOptions options) {
        this(DefaultExecutionContext.create(options), true);
    }

    public FlowEngine(ExecutionContext context) {
        this(context, false);
    }

    private FlowEngine(ExecutionContext context, boolean ownsContext) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
        this.ownsContext = ownsContext;
    }

    public <I, O> O run(Way<I, O> way, I input) {
        return way.execute(input, context);
    }

    public <I, O> O run(FlowDefinition<I, O> definition, I input) {
        return run(definition.way(), input);
    }

    @Override
    public void close() {
        if (ownsContext) {
            context.close();
        }
    }
}
