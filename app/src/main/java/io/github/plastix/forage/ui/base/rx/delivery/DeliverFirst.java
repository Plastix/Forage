package io.github.plastix.forage.ui.base.rx.delivery;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

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
                        new Func2<Boolean, T, T>() {
                            @Override
                            public T call(Boolean flag, T value) {
                                return flag ? value : null;
                            }
                        })
                .filter(new Func1<T, Boolean>() {
                    @Override
                    public Boolean call(T value) {
                        return value != null;
                    }
                })
                .take(1);
    }
}