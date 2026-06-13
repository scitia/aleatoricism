package io.github.scitia.aleatoricism.flows.engine;

import io.github.scitia.aleatoricism.flows.api.Datapoint;
import io.github.scitia.aleatoricism.flows.api.EmissionPoint;
import io.github.scitia.aleatoricism.flows.api.Waypoint;
import io.github.scitia.aleatoricism.flows.way.Way;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class BusinessFlowBuilder {

    private BusinessFlowBuilder() {
    }

    public static final class Store<S> {

        private S store;

        public Store(Class<S> store) throws InstantiationException,
                IllegalAccessException,
                NoSuchMethodException,
                InvocationTargetException {
            this.store = store.getDeclaredConstructor().newInstance();
        }

        public S getStore() {
            return store;
        }

        public void setStore(S store) {
            this.store = store;
        }
    }

    public static Definition define() {
        return new Definition();
    }

    public static final class Definition {

        private Definition() {
        }

        public <I, O, S> Path<I, O, S> start(Waypoint<I, O> waypoint) {
            return new Path<>(Way.step(waypoint));
        }

        public <I, O, S> Path<I, O, S> start(Datapoint<I, O> datapoint) {
            return new Path<>(Way.datapoint(datapoint));
        }

        public <I, O, S> Path<I, O, S> start(Way<I, O> way) {
            return new Path<>(way);
        }
    }

    public static final class Path<I, O, S> {

        private Store<S> store;

        private final Way<I, O> current;

        private Path(Way<I, O> current) {
            this.current = Objects.requireNonNull(current, "current cannot be null");
        }

        public <N> Path<I, N, S> then(Way<O, N> next, Consumer<S> storeSetup) {
            storeSetup.accept(store.getStore());
            return new Path<>(current.then(next));
        }

        public <N> Path<I, N, S> then(Waypoint<O, N> next, S storeSetup) {
            this.store.setStore(storeSetup);
            return then(Way.step(next));
        }

        public <N> Path<I, N, S> then(Datapoint<O, N> next) {
            return then(Way.datapoint(next));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Way<O, L> left, Way<O, R> right) {
            return then(Way.parallel(left, right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Waypoint<O, L> left, Waypoint<O, R> right) {
            return parallel(Way.step(left), Way.step(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Datapoint<O, L> left, Datapoint<O, R> right) {
            return parallel(Way.datapoint(left), Way.datapoint(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Way<O, L> left, Waypoint<O, R> right) {
            return parallel(left, Way.step(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Waypoint<O, L> left, Way<O, R> right) {
            return parallel(Way.step(left), right);
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Way<O, L> left, Datapoint<O, R> right) {
            return parallel(left, Way.datapoint(right));
        }

        public <L, R> Path<I, Map.Entry<L, R>, S> parallel(Datapoint<O, L> left, Way<O, R> right) {
            return parallel(Way.datapoint(left), right);
        }

        public Path<I, O, S> emit(EmissionPoint<O> sideEffect) {
            return new Path<>(current.emit(sideEffect));
        }

        public Way<I, O> buildWay() {
            return current;
        }

        public <S> Flow<I, O, S> withStore(Class<S> storeType) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
            Store<S>  store = new Store<>(storeType);
            return new Flow<>(current, store.getStore());
        }
    }
}
