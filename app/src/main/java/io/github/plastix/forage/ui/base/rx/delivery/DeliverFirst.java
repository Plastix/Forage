package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;

/**
 * From https://github.com/alapshin/arctor
 */
public class DeliverFirst<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverFirst(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        view,
                        observable,
                        (flag, value) -> flag ? value : null)
                .filter(value -> value != null)
                .take(1);
    }
}