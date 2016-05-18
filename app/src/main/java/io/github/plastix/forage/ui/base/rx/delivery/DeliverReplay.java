package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

/**
 * From https://github.com/alapshin/arctor
 */
public class DeliverReplay<T> implements Observable.Transformer<T, T> {

    private final Observable<Boolean> view;

    public DeliverReplay(Observable<Boolean> view) {
        this.view = view;
    }

    @Override
    public Observable<T> call(Observable<T> observable) {
        final ReplaySubject<T> subject = ReplaySubject.create();
        final Subscription subscription = observable.subscribe(subject);
        return view
                .switchMap(flag -> flag ? subject : Observable.<T>never())
                .doOnUnsubscribe(subscription::unsubscribe);
    }
}