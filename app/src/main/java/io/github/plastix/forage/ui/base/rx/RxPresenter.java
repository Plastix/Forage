package io.github.plastix.forage.ui.base.rx;

import io.github.plastix.forage.ui.base.Presenter;
import io.github.plastix.forage.ui.base.rx.delivery.DeliverFirst;
import io.github.plastix.forage.ui.base.rx.delivery.DeliverLatest;
import io.github.plastix.forage.ui.base.rx.delivery.DeliverLatestCache;
import io.github.plastix.forage.ui.base.rx.delivery.DeliverReplay;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter that provides support for "pausing" and "resuming" Observables and automatically
 * unsubscribing from subscriptions when the presenter is destroyed.
 * <p/>
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
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p/>
     * {@link #deliverFirst} delivers only the first onNext value that has been emitted by the source observable.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverFirst<T> deliverFirst() {
        return new DeliverFirst<>(viewLifecycle);
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p/>
     * {@link #deliverLatest} keeps the latest onNext value and emits it when there is attached view.
     * Terminates when source observable completes {link rx.Observable#onCompleted()}
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverLatest<T> deliverLatest() {
        return new DeliverLatest<>(viewLifecycle);
    }


    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p/>
     * {@link #deliverLatestCache} keeps the latest onNext value and emits it if there is * attached view. Never completes.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverLatestCache<T> deliverLatestCache() {
        return new DeliverLatestCache<>(viewLifecycle);
    }

    /**
     * Returns an {@link rx.Observable.Transformer} that delays emission from the source {@link rx.Observable}.
     * <p/>
     * {@link #deliverReplay} keeps all onNext values and emits them each time a new view gets attached.
     *
     * @param <T> the type of source observable emissions
     */
    public <T> DeliverReplay<T> deliverReplay() {
        return new DeliverReplay<>(viewLifecycle);
    }

}
