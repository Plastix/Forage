package io.github.plastix.forage.data;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class SingleObservableUseCase<T> extends UseCase {

    public void executeMainThread(SingleSubscriber<T> subscriber) {
        this.subscription = this.buildSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected abstract Single<T> buildSingle();

    public void executeInBackground(SingleSubscriber<T> subscriber) {
        this.subscription = this.buildSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
