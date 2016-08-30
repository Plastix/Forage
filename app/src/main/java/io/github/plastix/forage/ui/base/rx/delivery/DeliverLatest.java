package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;

/**
 * {@link rx.Observable.Transformer} that couples data and view status
 * <p>
 * If view is attached (latest emitted value from view observable is true) then values from data
 * observable propagates us usual.
 * <p>
 * If view is detached (latest emitted value from view observable is false) then values from data
 * observable propagates using following rules:
 * <ul>
 * <li>If data observable emits onError then it would be delivered after view is attached</li>
 * <li>If data observable emits onCompleted then after view is attached last onNext value from
 * data observable is delivered followed by onCompleted event</li>
 * <li>If data observable emits multiple values then after view is attached last emitted value
 * is delivered</li>
 * </ul>
 * <p>
 * Adapted from https://github.com/alapshin/arctor (MIT License)
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
                        // Materialize data Observable to handle onError and onCompleted events when view is detached
                        observable.materialize()
                                // Delay completed notifications until the view reattaches
                                .delay(notification -> {
                                    if (notification.isOnCompleted()) {
                                        return view.first(view1 -> view1);
                                    } else {
                                        // Pass all other events downstream immediately
                                        // They will be "cached" by combineLatest
                                        return Observable.empty();
                                    }
                                }),
                        // Pass notification downstream if view is attached, otherwise null
                        (isViewAttached, notification) -> isViewAttached ? notification : null)
                //  Filter out null events to ensure we only emit when the view is attached
                .filter(value -> value != null)
                // Convert our notifications back into values
                .dematerialize();
    }
}