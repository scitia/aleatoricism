package io.github.scitia.aleatoricism.flows.execution;

/**
 * Runtime options for business flow execution.
 */
public record ExecutionOptions(boolean waitForSideEffects) {

    public static ExecutionOptions defaults() {
        return new ExecutionOptions(false);
    }

    public static ExecutionOptions waitForAll() {
        return new ExecutionOptions(true);
    }
}
