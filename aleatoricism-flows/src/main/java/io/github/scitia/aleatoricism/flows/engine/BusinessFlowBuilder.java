package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.api.Datapoint;
import io.github.scitia.aleatoricism.flows.api.EmissionPoint;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.way.Way;

import java.util.Map;
import java.util.Objects;

/**
 * Typed builder for readable decomposition-based business flow construction.
 */
public final class BusinessFlowBuilder {

    private BusinessFlowBuilder() {
    }

    public static Definition define() {
        return new Definition();
    }

    public static final class Definition {

        private Definition() {
        }

        public <I, O> Path<I, O> start(Waypoint<I, O> waypoint) {
            return new Path<>(Way.step(waypoint));
        }

        public <I, O> Path<I, O> start(Datapoint<I, O> datapoint) {
            return new Path<>(Way.datapoint(datapoint));
        }

        public <I, O> Path<I, O> start(Way<I, O> way) {
            return new Path<>(way);
        }
    }

    public static final class Path<I, O> {

        private final Way<I, O> current;

        private Path(Way<I, O> current) {
            this.current = Objects.requireNonNull(current, "current cannot be null");
        }

        public <N> Path<I, N> then(Way<O, N> next) {
            return new Path<>(current.then(next));
        }

        public <N> Path<I, N> then(Waypoint<O, N> next) {
            return then(Way.step(next));
        }

        public <N> Path<I, N> then(Datapoint<O, N> next) {
            return then(Way.datapoint(next));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Way<O, L> left, Way<O, R> right) {
            return then(Way.parallel(left, right));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Waypoint<O, L> left, Waypoint<O, R> right) {
            return parallel(Way.step(left), Way.step(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Datapoint<O, L> left, Datapoint<O, R> right) {
            return parallel(Way.datapoint(left), Way.datapoint(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Way<O, L> left, Waypoint<O, R> right) {
            return parallel(left, Way.step(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Waypoint<O, L> left, Way<O, R> right) {
            return parallel(Way.step(left), right);
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Way<O, L> left, Datapoint<O, R> right) {
            return parallel(left, Way.datapoint(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>> parallel(Datapoint<O, L> left, Way<O, R> right) {
            return parallel(Way.datapoint(left), right);
        }

        public Path<I, O> emit(EmissionPoint<O> sideEffect) {
            return new Path<>(current.emit(sideEffect));
        }

        public Way<I, O> buildWay() {
            return current;
        }

        public Flow<I, O> build() {
            return new Flow<>(current);
        }
    }
}
