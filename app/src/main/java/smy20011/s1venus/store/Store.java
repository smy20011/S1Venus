package smy20011.s1venus.store;

/**
 * Have to define the store interface in java thus kotlin will recognize type of function.
 */
public interface Store {
    interface Action {
        String getActionType();
    }

    interface State {
    }

    interface Reducer {
        State apply(Action action, State state);
    }

    interface Dispatch {
        State apply(Action action, Store store);
    }

    interface Middleware {
        Dispatch apply(Dispatch dispatch);
    }

    interface Subscriber {
        void onChanged();
    }

    interface Subscription {
        void leave();
    }

    Subscription subscribe(Subscriber subscriber);
    State getState();
    Reducer getReducer();
}
