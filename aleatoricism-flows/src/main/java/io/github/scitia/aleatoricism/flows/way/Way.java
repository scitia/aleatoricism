package io.github.scitia.aleatoricism.flows.way;

import io.github.scitia.aleatoricism.flows.api.EmissionPoint;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.DefaultExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;
import io.github.scitia.aleatoricism.flows.execution.FlowExecutionException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Algebraic data type that models a business path as a directed graph.
 */
public sealed interface Way<I, O>
    permits Way.Step, Way.Sequence, Way.Conditional, Way.Parallel, Way.SideEffect, Way.OutputSideEffect {

    O execute(I input, ExecutionContext context);

    default O execute(I input) {
        try (DefaultExecutionContext context = DefaultExecutionContext.create()) {
            return execute(input, context);
        }
    }

    default <N> Way<I, N> then(Way<O, N> next) {
        return new Sequence<>(this, next);
    }

    default Way<I, O> withSideEffect(EmissionPoint<I> sideEffect) {
        return new SideEffect<>(this, sideEffect);
    }

    default Way<I, O> emit(EmissionPoint<O> sideEffect) {
        return new OutputSideEffect<>(this, sideEffect);
    }

    static <I, O> Way<I, O> step(Waypoint<I, O> waypoint) {
        return new Step<>(waypoint);
    }

    static <I, O> Way<I, O> when(
            Predicate<I> condition,
            Way<I, O> whenTrue,
            Way<I, O> whenFalse
    ) {
        return new Conditional<>(condition, whenTrue, whenFalse);
    }

    static <I, L, R> Way<I, Map.Entry<L, R>> parallel(Way<I, L> left, Way<I, R> right) {
        return new Parallel<>(left, right);
    }

    record Step<I, O>(Waypoint<I, O> waypoint) implements Way<I, O> {

        public Step {
            Objects.requireNonNull(waypoint, "waypoint cannot be null");
        }

        @Override
        public O execute(I input, ExecutionContext context) {
            return waypoint.execute(input, context);
        }
    }

    record Sequence<I, M, O>(Way<I, M> first, Way<M, O> second) implements Way<I, O> {

        public Sequence {
            Objects.requireNonNull(first, "first cannot be null");
            Objects.requireNonNull(second, "second cannot be null");
        }

        @Override
        public O execute(I input, ExecutionContext context) {
            M intermediate = first.execute(input, context);
            return second.execute(intermediate, context);
        }
    }

    record Conditional<I, O>(
            Predicate<I> condition,
            Way<I, O> whenTrue,
            Way<I, O> whenFalse
    ) implements Way<I, O> {

        public Conditional {
            Objects.requireNonNull(condition, "condition cannot be null");
            Objects.requireNonNull(whenTrue, "whenTrue cannot be null");
            Objects.requireNonNull(whenFalse, "whenFalse cannot be null");
        }

        @Override
        public O execute(I input, ExecutionContext context) {
            return condition.test(input) ? whenTrue.execute(input, context) : whenFalse.execute(input, context);
        }
    }

    record Parallel<I, L, R>(Way<I, L> left, Way<I, R> right) implements Way<I, Map.Entry<L, R>> {

        public Parallel {
            Objects.requireNonNull(left, "left cannot be null");
            Objects.requireNonNull(right, "right cannot be null");
        }

        @Override
        public Map.Entry<L, R> execute(I input, ExecutionContext context) {
            CompletableFuture<L> leftFuture = CompletableFuture.supplyAsync(
                    () -> left.execute(input, context),
                    context.executor()
            );
            CompletableFuture<R> rightFuture = CompletableFuture.supplyAsync(
                    () -> right.execute(input, context),
                    context.executor()
            );

            try {
                return Map.entry(leftFuture.join(), rightFuture.join());
            } catch (Exception exception) {
                throw new FlowExecutionException("Parallel branch execution failed", exception);
            }
        }
    }

    record SideEffect<I, O>(Way<I, O> main, EmissionPoint<I> sideEffect) implements Way<I, O> {

        public SideEffect {
            Objects.requireNonNull(main, "main cannot be null");
            Objects.requireNonNull(sideEffect, "sideEffect cannot be null");
        }

        @Override
        public O execute(I input, ExecutionContext context) {
            CompletableFuture<Void> sideEffectFuture = CompletableFuture.runAsync(
                    () -> sideEffect.execute(input, context),
                    context.executor()
            );
            O result = main.execute(input, context);
            if (context.options().waitForSideEffects()) {
                sideEffectFuture.join();
            }
            return result;
        }
    }

    record OutputSideEffect<I, O>(Way<I, O> main, EmissionPoint<O> sideEffect) implements Way<I, O> {

        public OutputSideEffect {
            Objects.requireNonNull(main, "main cannot be null");
            Objects.requireNonNull(sideEffect, "sideEffect cannot be null");
        }

        @Override
        public O execute(I input, ExecutionContext context) {
            O result = main.execute(input, context);
            CompletableFuture<Void> sideEffectFuture = CompletableFuture.runAsync(
                    () -> sideEffect.execute(result, context),
                    context.executor()
            );
            if (context.options().waitForSideEffects()) {
                sideEffectFuture.join();
            }
            return result;
        }
    }
}
