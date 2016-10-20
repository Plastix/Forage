package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;
import rx.subscriptions.Subscriptions;

/**
 * Adapted from https://github.com/alapshin/arctor (MIT License)
 */
public class DeliverReplay<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverReplay(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        final ReplaySubject<T> subject = ReplaySubject.create();
        // 1 element array is a hack fix to allow use in doOnSubscribe lambda expression
        final Subscription[] subscription = {Subscriptions.unsubscribed()};
        return view
                .switchMap(flag -> flag ? subject : Observable.never())
                .doOnUnsubscribe(subscription[0]::unsubscribe)
                .doOnSubscribe(() -> subscription[0] = observable.subscribe(subject));
    }
}