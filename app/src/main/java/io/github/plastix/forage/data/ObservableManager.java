package io.github.plastix.forage.data;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action0;

/**
 * Singleton instance to store Rx Observables outside of the Activity/Fragment lifecycle.
 */
@Singleton
public class ObservableManager {

    private Map<String, Observable<?>> observableMap;

    @Inject
    public ObservableManager() {
        observableMap = new HashMap<>();
    }

    /**
     * Store the specified observable using the specified key. The observable is cached using
     * Rx's default .cache() operator.
     * <p/>
     * See {@link Observable#cache()} for caveats.
     *
     * @param id         Unique key to store Observable by.
     * @param observable Observable to store.
     */
    public void storeObservable(final String id, Observable<?> observable) {
        // Automatically remove the observable once it is completed
        observable = observable.doOnTerminate(new Action0() {
            @Override
            public void call() {
                removeObservable(id);
            }
        }).cache();

        observableMap.put(id, observable);
    }

    /**
     * Removes the specified Observable.
     *
     * @param id Key of observable to remove.
     */
    public void removeObservable(String id) {
        observableMap.remove(id);
    }

    /**
     * Returns the Observable stored in the manager, if there is one.
     *
     * @param id  Key of Observable to fetch.
     * @param <T> Generic type of observable.
     * @return Observable stored or null if it does not exist.
     */
    @SuppressWarnings("unchecked")
    public <T> Observable<T> getObservable(String id) {
        return (Observable<T>) observableMap.get(id);
    }

}
