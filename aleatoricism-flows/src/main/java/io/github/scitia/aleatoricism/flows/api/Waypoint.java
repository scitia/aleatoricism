package io.github.scitia.aleatoricism.flows.api;

import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.FlowExecutionException;
import jakarta.validation.Valid;

/**
 * Smallest executable unit in a business graph.
 */
@FunctionalInterface
public interface Waypoint<I, O, S> {

    O handle(@Valid I input, ExecutionContext<S> context) throws Exception;

    default O execute(@Valid I input, ExecutionContext<S> context) {
        try {
            return handle(input, context);
        } catch (FlowExecutionException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new FlowExecutionException("Waypoint execution failed", exception);
        }
    }
}
