package io.github.scitia.aleatoricism.flows.way;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.execution.ExecutionContext;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

public interface Way<I, O, S> {

    O execute(I input, ExecutionContext<S> context);

    default <N> Way<I, N, S> then(Way<O, N, S> next) {
        return (input, ctx) -> {
            O result = this.execute(input, ctx);
            return next.execute(result, ctx);
        };
    }

    default Way<I, O, S> emit(ConsumerWithContext<O, S> consumer) {
        return (input, ctx) -> {
            O result = this.execute(input, ctx);
            consumer.accept(result, ctx);
            return result;
        };
    }

    static <I, O, S> Way<I, O, S> when(
            Predicate<I> condition,
            Way<I, O, S> positive,
            Way<I, O, S> negative
    ) {
        return (input, ctx) -> {
            if (condition.test(input)) {
                return positive.execute(input, ctx);
            } else {
                return negative.execute(input, ctx);
            }
        };
    }

    static <I, L, R, S> Way<I, Map.Entry<L, R>, S> parallel(
            Way<I, L, S> left,
            Way<I, R, S> right
    ) {
        return (input, ctx) -> {

            Executor executor = ctx.executor();

            CompletableFuture<L> lf =
                    CompletableFuture.supplyAsync(() -> left.execute(input, ctx), executor);

            CompletableFuture<R> rf =
                    CompletableFuture.supplyAsync(() -> right.execute(input, ctx), executor);

            return Map.entry(lf.join(), rf.join());
        };
    }

    static <I, O, S> Way<I, O, S> from(Waypoint<I, O, S> waypoint) {
        return waypoint::execute;
    }

    static <T, S> Way<T, T, S> identity() {
        return (input, ctx) -> input;
    }
}