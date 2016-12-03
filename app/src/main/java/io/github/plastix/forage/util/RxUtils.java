package io.github.plastix.forage.util;

import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Utility class for Reactive Extensions.
 */
public class RxUtils {

    private RxUtils() {
        throw new UnsupportedOperationException("No Instantiation!");
    }

    /**
     * Unsubscribes from the specified subscription.
     *
     * @param subscription Subscription instance.
     */
    public static void safeUnsubscribe(@Nullable Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * Returns a new Observable transformer that subscribes on a IO thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Observable.Transformer<T, T> subscribeOnIoThreadTransformer() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io());
    }

    /**
     * Returns a new Single transformer that subscribes on a IO thread.
     *
     * @param <T> Generic type of single.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> subscribeOnIoThreadTransformerSingle() {
        return tSingle -> tSingle.subscribeOn(Schedulers.io());
    }

    /**
     * Returns a new Observable transformer that observes on the Android Main thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Observable.Transformer<T, T> observeOnUIThreadTransformer() {
        return tObservable -> tObservable.observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Returns a new Single transformer that observes on the Android Main thread.
     *
     * @param <T> Generic type of single.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> observeOnUIThreadTransformerSingle() {
        return tSingle -> tSingle.observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Returns a new Observable transformer that subscribes on a CPU computation thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Observable.Transformer<T, T> subscribeOnComputationThreadTransformer() {
        return tObservable -> tObservable.subscribeOn(Schedulers.computation());
    }

    /**
     * Returns a new Single transformer that subscribes on a CPU computation thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> subscribeOnComputationThreadTransformerSingle() {
        return tSingle -> tSingle.subscribeOn(Schedulers.computation());
    }

    /**
     * Transformer which calls the specified Action1 upon first emission from the source observable.
     */
    public static <T> Observable.Transformer<T, T> doOnFirst(Action1<? super T> action) {
        return o -> Observable.defer(() -> {
            final AtomicBoolean first = new AtomicBoolean(true);
            return o.doOnNext(t -> {
                if (first.compareAndSet(true, false)) {
                    action.call(t);
                }
            });
        });
    }

}
