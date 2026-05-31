package io.github.scitia.aleatoricism.flows.api;

import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.FlowExecutionException;

/**
 * Smallest executable unit in a business graph.
 */
@FunctionalInterface
public interface Waypoint<I, O> {

    O handle(I input, ExecutionContext context) throws Exception;

    default O execute(I input, ExecutionContext context) {
        try {
            return handle(input, context);
        } catch (FlowExecutionException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new FlowExecutionException("Waypoint execution failed", exception);
        }
    }
}
