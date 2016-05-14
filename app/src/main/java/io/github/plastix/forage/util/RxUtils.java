package io.github.plastix.forage.util;

import android.support.annotation.Nullable;

import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * Returns a new Single transformer that subscribes on a IO thread.
     *
     * @param <T> Generic type of single.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> subscribeOnIoThreadTransformerSingle() {
        return new Single.Transformer<T, T>() {
            @Override
            public Single<T> call(Single<T> tSingle) {
                return tSingle.subscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * Returns a new Observable transformer that observes on the Android Main thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Observable.Transformer<T, T> observeOnUIThreadTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * Returns a new Single transformer that observes on the Android Main thread.
     *
     * @param <T> Generic type of single.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> observeOnUIThreadTransformerSingle() {
        return new Single.Transformer<T, T>() {
            @Override
            public Single<T> call(Single<T> tSingle) {
                return tSingle.observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * Returns a new Observable transformer that subscribes on a CPU computation thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Observable.Transformer<T, T> subscribeOnComputationThreadTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.computation());
            }
        };
    }

    /**
     * Returns a new Single transformer that subscribes on a CPU computation thread.
     *
     * @param <T> Generic type of observable.
     * @return New transformer.
     */
    public static <T> Single.Transformer<T, T> subscribeOnComputationThreadTransformerSingle() {
        return new Single.Transformer<T, T>() {
            @Override
            public Single<T> call(Single<T> tSingle) {
                return tSingle.subscribeOn(Schedulers.computation());
            }
        };
    }

}
