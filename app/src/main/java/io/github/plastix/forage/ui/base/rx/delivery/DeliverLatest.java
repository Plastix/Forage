package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;

/**
 * From https://github.com/alapshin/arctor
 */
public class DeliverLatest<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverLatest(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        return Observable
                .combineLatest(
                        view,
                        observable
                                .materialize(),
                        (flag, notification) -> flag ? notification : null)
                .filter(notification -> notification != null)
                .dematerialize();
    }
}