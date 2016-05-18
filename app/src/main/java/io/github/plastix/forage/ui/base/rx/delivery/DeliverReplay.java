package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
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
                .switchMap(new Func1<Boolean, Observable<T>>() {
                    @Override
                    public Observable<T> call(final Boolean flag) {
                        return flag ? subject : Observable.<T>never();
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        subscription.unsubscribe();
                    }
                });
    }
}