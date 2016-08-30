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
        // This is nearly identical to DeliverLatest except we call take(1) on the data observable first
        // See DeliverLatest transformer!
        return Observable
                .combineLatest(
                        view,
                        observable.take(1)
                                .materialize()
                                .delay(notification -> {
                                    if (notification.isOnCompleted()) {
                                        return view.first(view1 -> view1);
                                    } else {
                                        return Observable.empty();
                                    }
                                }),
                        (isViewAttached, notification) -> isViewAttached ? notification : null)
                .filter(value -> value != null)
                .dematerialize();
    }
}