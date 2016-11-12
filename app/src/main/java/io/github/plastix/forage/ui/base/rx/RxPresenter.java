package io.github.plastix.forage.ui.base.rx;

import io.github.plastix.forage.ui.base.Presenter;
import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter that provides support for "pausing" and "resuming" Observables and automatically
 * unsubscribing from subscriptions when the presenter is destroyed.
 * <p>
 * Slightly adapted Code from https://github.com/alapshin/arctor
 *
 * @param <V> Generic type of view that the presenter interacts with.
 */
public abstract class RxPresenter<V> extends Presenter<V> {

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private BehaviorSubject<Boolean> viewLifecycle = BehaviorSubject.create();

    @Override
    public void onViewAttached(V view) {
        super.onViewAttached(view);
        viewLifecycle.onNext(true);
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        viewLifecycle.onNext(false);
    }

    @Override
    public void onDestroyed() {
        viewLifecycle.onCompleted();
        clearSubscriptions();
    }

    /**
     * Removes and unsubscribes from all subscriptions that have been registered with
     * {@link #addSubscription(Subscription)} previously.
     * See {@link CompositeSubscription#clear() for details.}
     */
    public void clearSubscriptions() {
        subscriptions.clear();
    }

    /**
     * Registers a subscription to automatically be unsubscribed on onDestroy.
     * See {@link CompositeSubscription#add(Subscription) for details.}
     *
     * @param subscription A Subscription to add.
     */
    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    /**
     * Removes and unsubscribes a subscription that has been previously registered with
     * {@link #addSubscription(Subscription)}.
     * See {@link CompositeSubscription#remove(Subscription) for details.}
     *
     * @param subscription a subscription to remove.
     */
    public void removeSubscription(Subscription subscription) {
        subscriptions.remove(subscription);
    }

    /**
     * Exposes the state of the attached view as a boolean Observabele. True is emitted when the view
     * is attached and false is emitted when the view detatches.
     */
    public Observable<Boolean> getViewState() {
        return viewLifecycle.asObservable();
    }

}
