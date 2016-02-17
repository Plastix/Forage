package io.github.plastix.forage.util;

import rx.Subscription;

/**
 * Utility class for Reactive Extensions.
 */
public class RxUtils {

    private RxUtils() {
        // No instantiation
    }

    public static void safeUnsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
