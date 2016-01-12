package io.github.plastix.forage.data;


import rx.Subscription;
import rx.subscriptions.Subscriptions;

public abstract class UseCase {

    protected Subscription subscription;

    public UseCase() {
        this.subscription = Subscriptions.empty();
    }


    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
    }
}

