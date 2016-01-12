package io.github.plastix.forage.data;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class ObservableUseCase<T> extends UseCase {

    public void executeMainThread(Subscriber<T> subscriber) {
        this.subscription = this.buildObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected abstract Observable<T> buildObservable();

    public void executeInBackground(Subscriber<T> subscriber) {
        this.subscription = this.buildObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
