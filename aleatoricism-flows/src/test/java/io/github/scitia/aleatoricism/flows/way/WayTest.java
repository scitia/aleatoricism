package io.github.scitia.aleatoricism.flows.way;

import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.aleatoricism.flows.execution.ExecutionOptions;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WayTest {

    @Test
    void shouldRunSequentialWaypoints() {
        Way<String, Integer> flow = Way.step((String input, ExecutionContext _) -> input.trim())
                .then(Way.step((String trimmed, ExecutionContext _) -> trimmed.length()));

        try (FlowEngine engine = new FlowEngine()) {
            Integer result = engine.run(flow, "  alea  ");
            assertEquals(4, result);
        }
    }

    @Test
    void shouldChooseBranchBasedOnCondition() {
        Way<Integer, String> flow = Way.when(
                value -> value > 0,
                Way.step((Integer _, ExecutionContext _) -> "POSITIVE"),
                Way.step((Integer _, ExecutionContext _) -> "NON_POSITIVE")
        );

        try (FlowEngine engine = new FlowEngine()) {
            assertEquals("POSITIVE", engine.run(flow, 10));
            assertEquals("NON_POSITIVE", engine.run(flow, -1));
        }
    }

    @Test
    void shouldRunParallelSubPaths() {
        Way<String, Map.Entry<Integer, String>> flow = Way.parallel(
                Way.step((String input, ExecutionContext _) -> input.length()),
                Way.step((String input, ExecutionContext _) -> input.toUpperCase())
        );

        try (FlowEngine engine = new FlowEngine()) {
            Map.Entry<Integer, String> result = engine.run(flow, "alea");
            assertEquals(4, result.getKey());
            assertEquals("ALEA", result.getValue());
        }
    }

    @Test
    void shouldWaitForSideEffectWhenConfigured() {
        AtomicBoolean emitted = new AtomicBoolean(false);

        Way<String, String> flow = Way.step((String input, ExecutionContext _) -> input + "-done")
                .withSideEffect((String _, ExecutionContext _) -> {
                    emitted.set(true);
                    return null;
                });

        try (FlowEngine engine = new FlowEngine(ExecutionOptions.waitForAll())) {
            String result = engine.run(flow, "task");
            assertEquals("task-done", result);
            assertTrue(emitted.get());
        }
    }
}
