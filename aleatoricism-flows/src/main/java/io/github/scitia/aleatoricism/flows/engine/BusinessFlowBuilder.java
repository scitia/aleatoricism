package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.way.ConsumerWithContext;
import io.github.scitia.aleatoricism.flows.way.Way;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class BusinessFlowBuilder {

    private BusinessFlowBuilder() {}

    public static Definition define() {
        return new Definition();
    }

    public static final class Definition {

        private Definition() {}

        public <I, O, S> Path<I, O, S> start(Waypoint<I, O, S> waypoint, Supplier<S> storeFactory) {
            return new Path<>(Way.from(waypoint), storeFactory);
        }

        public <I, O, S> Path<I, O, S> start(Way<I, O, S> way, Supplier<S> storeFactory) {
            return new Path<>(way, storeFactory);
        }
    }

    public static final class Path<I, O, S> {

        private final Way<I, O, S> current;
        private final Supplier<S> storeFactory;

        private Path(Way<I, O, S> current,  Supplier<S> storeFactory) {
            this.current = Objects.requireNonNull(current);
            this.storeFactory = Objects.requireNonNull(storeFactory);
        }

        public <N> Path<I, N, S> then(Way<O, N, S> next) {
            return new Path<>(current.then(next), storeFactory);
        }

        public <N> Path<I, N, S> then(Waypoint<O, N, S> waypoint) {
            return then(Way.from(waypoint));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(
                Way<O, L, S> left,
                Way<O, R, S> right
        ) {
            return then(Way.parallel(left, right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(
                Waypoint<O, L, S> left,
                Waypoint<O, R, S> right
        ) {
            return parallel(Way.from(left), Way.from(right));
        }

        public Path<I, O, S> emit(ConsumerWithContext<O, S> consumer) {
            return new Path<>(current.emit(consumer), storeFactory);
        }

        public Flow<I, O, S> build() {
            return new Flow<>(current, storeFactory.get());
        }
    }
}