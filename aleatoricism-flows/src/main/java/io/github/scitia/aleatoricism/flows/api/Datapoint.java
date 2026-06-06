package io.github.scitia.aleatoricism.flows.api;

import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.FlowExecutionException;

/**
 * Data access node supporting dependency injection.
 * Unlike Waypoint which uses Singleton pattern, Datapoint is designed
 * for framework-managed instances (Spring beans) that can have dependencies injected.
 */
@FunctionalInterface
public interface Datapoint<I, O> {

    O handle(I input, ExecutionContext context) throws Exception;

    default O execute(I input, ExecutionContext context) {
        try {
            return handle(input, context);
        } catch (FlowExecutionException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new FlowExecutionException("Datapoint execution failed", exception);
        }
    }
}

