package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;

/**
 * Transformer which couples data Observable with view Observable.
 * <p>
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
public class DeliverFirst<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverFirst(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return observable.take(1)
                .compose(new DeliverLatest<>(view));
    }
}