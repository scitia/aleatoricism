package io.github.scitia.aleatoricism.flows.execution;

public class FlowExecutionException extends RuntimeException {

    public FlowExecutionException(String message) {
        super(message);
    }

    public FlowExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
