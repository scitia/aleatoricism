package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.execution.DefaultExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.ExecutionOptions;
import io.github.scitia.aleatoricism.flows.way.Way;

import java.util.Objects;

/**
 * Executes a business flow inside a shared execution context.
 */
public final class FlowEngine {

    private final ExecutionOptions options;

    public FlowEngine() {
        this(ExecutionOptions.defaults());
    }

    public FlowEngine(ExecutionOptions options) {
        this.options = Objects.requireNonNull(options, "executionOptions cannot be null");
    }

    public <I, O, S> O run(Way<I, O, S> way, I input, S store) {
        try (ExecutionContext<S> context = DefaultExecutionContext.create(options, store)) {
            return way.execute(input, context);
        }
    }

    public <I, O, S> O run(Flow<I, O, S> flow, I input) {
        try (ExecutionContext<S> ctx = DefaultExecutionContext.create(flow.getStore())) {
            return flow.getWay().execute(input, ctx);
        }
    }
}
