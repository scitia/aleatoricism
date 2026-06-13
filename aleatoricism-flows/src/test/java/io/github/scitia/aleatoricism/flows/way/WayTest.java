package io.github.scitia.aleatoricism.flows.way;

import io.github.scitia.aleatoricism.flows.engine.FlowEngine;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.ExecutionOptions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WayTest {

    static class TestStore {
    }

    @Test
    void shouldRunSequentialWaypoints() {
        Way<String, Integer, TestStore> flow =
                Way.from((String input, ExecutionContext<TestStore> ctx) -> input.trim())
                        .then(Way.from((String trimmed, ExecutionContext<TestStore> ctx) -> trimmed.length()));

        FlowEngine engine = new FlowEngine();

        Integer result = engine.run(flow, "  alea  ", new TestStore());

        assertEquals(4, result);
    }

    @Test
    void shouldChooseBranchBasedOnCondition() {
        Way<Integer, String, TestStore> flow = Way.when(
                value -> value > 0,
                Way.from((Integer i, ExecutionContext<TestStore> ctx) -> "POSITIVE"),
                Way.from((Integer i, ExecutionContext<TestStore> ctx) -> "NON_POSITIVE")
        );

        FlowEngine engine = new FlowEngine();

        assertEquals("POSITIVE",
                engine.run(flow, 10, new TestStore()));

        assertEquals("NON_POSITIVE",
                engine.run(flow, -1, new TestStore()));
    }

    @Test
    void shouldRunParallelSubPaths() {
        Way<String, Map.Entry<Integer, String>, TestStore> flow =
                Way.parallel(
                        Way.from((String input, ExecutionContext<TestStore> ctx) -> input.length()),
                        Way.from((String input, ExecutionContext<TestStore> ctx) -> input.toUpperCase())
                );

        FlowEngine engine = new FlowEngine();

        Map.Entry<Integer, String> result =
                engine.run(flow, "alea", new TestStore());

        assertEquals(4, result.getKey());
        assertEquals("ALEA", result.getValue());
    }

    @Test
    void shouldWaitForSideEffectWhenConfigured() {
        AtomicBoolean emitted = new AtomicBoolean(false);

        Way<String, String, TestStore> flow =
                Way.from((String input, ExecutionContext<TestStore> ctx) -> input + "-done")
                        .emit((String value, ExecutionContext<TestStore> ctx) -> {
                            emitted.set(true);
                        });

        FlowEngine engine = new FlowEngine(ExecutionOptions.waitForAll());

        String result = engine.run(flow, "task", new TestStore());

        assertEquals("task-done", result);
        assertTrue(emitted.get());
    }
}