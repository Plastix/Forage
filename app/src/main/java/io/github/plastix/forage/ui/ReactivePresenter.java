package io.github.plastix.forage.ui;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.github.plastix.forage.data.ObservableManager;
import io.github.plastix.forage.util.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Abstract Presenter that holds a subscription and observable. This class provides automatic
 * caching of observables.
 *
 * @param <T> View type of the presenter
 * @param <O> Observable type.
 */
public abstract class ReactivePresenter<T extends View, O> extends Presenter<T> {

    protected ObservableManager cache;
    protected Subscription subscription;
    protected Observable<O> request;

    public ReactivePresenter() {
        this.subscription = Subscriptions.empty();
    }

    /**
     * Automatically set the OberverableManager with Dagger 2 method injection.
     *
     * @param manager Dependency from the graph.
     */
    @Inject
    public void setCache(@NonNull ObservableManager manager) {
        this.cache = manager;
    }


    /**
     * Caches and subscribes to the observable.
     */
    protected void subscribe() {
        if (!cache.isStored(getObservableId())) {
            cache.storeObservable(getObservableId(), request);
        }
        this.subscription = request.subscribe(buildSubscription());
    }


    public void unsubscribe() {
        RxUtils.safeUnsubscribe(subscription);
    }

    @Override
    public void onResume() {
        String id = getObservableId();
        // Existing cached observable was found
        if (cache.isStored(id)) {
            // Attach, and resubscribe
            this.request = cache.getObservable(id);
            subscribe();

            onAttachObservable();

        } else {
            // Create a brand new observable
            // Don't forget to remove from the cache when the observable terminates!
            this.request = buildObservable().doOnTerminate(new Action0() {
                @Override
                public void call() {
                    cache.removeObservable(getObservableId());
                }
            });
        }
    }

    @Override
    public void onPause() {
        unsubscribe();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart() {

    }

    /**
     * Callback that the subclass can override if it needs to run an action when an existing observable
     * is attached to the presenter.
     */
    protected void onAttachObservable() {

    }

    protected abstract String getObservableId();

    protected abstract Subscriber<O> buildSubscription();

    protected abstract Observable<O> buildObservable();

}
