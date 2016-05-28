package smy20011.s1venus.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store<T> {
    interface Action {
        String getTag();
    }

    interface Reducer<T> {
        T reduce(Action action, T data);
    }

    static class ComposedReducer<T> implements Reducer<T> {
        List<Reducer<T>> reducers;

        public ComposedReducer(List<Reducer<T>> reducers) {
            this.reducers = reducers;
        }

        @Override
        public T reduce(Action action, T data) {
            T result = data;
            for (Reducer<T> reducer : reducers) {
                result = reducer.reduce(action, result);
            }
            return result;
        }
    }

    interface Dispatcher<T> {
        T dispatch(Action action, Store<T> store);
    }

    static class DummyDispatcher<T> implements Dispatcher<T> {
        @Override
        public T dispatch(Action action, Store<T> store) {
            return store.reducer.reduce(action, store.data);
        }
    }

    interface Middleware<T> {
        Dispatcher<T> wrap(Dispatcher<T> dispatcher);
    }

    interface Subscriber<T> {
        void onChange(T data);
    }

    public static class Subscription<T> {

        final Store<T> store;
        final Subscriber<T> subscriber;

        public Subscription(Store<T> store, Subscriber<T> subscriber) {
            this.store = store;
            this.subscriber = subscriber;
        }

        public void leave() {
            store.subscribers.remove(subscriber);
        }
    }

    final Reducer<T> reducer;
    final Dispatcher<T> dispatcher;
    final List<Subscriber<T>> subscribers = new ArrayList<>();
    T data;

    public Subscription<T> subscribe(Subscriber<T> subscriber) {
        subscribers.add(subscriber);
        return new Subscription<>(this, subscriber);
    }

    public Store(Reducer<T> reducer, Dispatcher<T> dispatcher) {
        this.reducer = reducer;
        this.dispatcher = dispatcher;
    }

    public static class Builder<T> {
        List<Reducer<T>> reducers = new ArrayList<>();
        Dispatcher<T> dispatcher = new DummyDispatcher<>();

        public Builder<T> withReducers(Reducer<T> ... reducers) {
            this.reducers.addAll(Arrays.asList(reducers));
            return this;
        }

        public Builder<T> withMiddlewares(Middleware<T> ... middlewares) {
            for (Middleware<T> middleware : middlewares) {
                dispatcher = middleware.wrap(dispatcher);
            }
            return this;
        }

        public Store<T> build() {
            return new Store<>(new ComposedReducer<>(reducers), dispatcher);
        }
    }
}
