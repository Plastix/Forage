package io.github.plastix.forage.util;

import org.junit.Test;

import rx.Subscription;

import static com.google.common.truth.Truth.assert_;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxUtilsTest {

    @Test
    public void safeUnsubscribe_handleNullSubscription() {
        try {
            RxUtils.safeUnsubscribe(null);
        } catch (Exception e) {
            assert_().fail("RxUtils safeUnsubscribe threw an unexpected error!", e);
        }
    }

    @Test
    public void safeUnsubscribe_unsubscribeSubscriptionCorrectly() {
        Subscription subscription = mock(Subscription.class);

        RxUtils.safeUnsubscribe(subscription);

        verify(subscription, times(1)).unsubscribe();
    }

    @Test
    public void safeUnsubscribe_onlyUnsubscribeActiveSubscriptions() {
        Subscription subscription = mock(Subscription.class);
        when(subscription.isUnsubscribed()).thenReturn(true);

        RxUtils.safeUnsubscribe(subscription);

        verify(subscription, never()).unsubscribe();
    }
}
