package io.github.plastix.forage.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.observers.TestSubscriber;

import static com.google.common.truth.Truth.assert_;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RxUtilsTest {

    TestSubscriber<Integer> testSubscriber;

    @Mock
    Action1<Integer> func;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testSubscriber = new TestSubscriber<>();
    }

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

    @Test
    public void doOnFirst_singleEmissionCallsFunction() {
        Observable.just(0)
                .compose(RxUtils.doOnFirst(func))
                .subscribe(testSubscriber);

        verify(func, times(1)).call(0);
    }

    @Test
    public void doOnFirst_doubleEmissionCallsFunction() {
        Observable.just(0,1)
                .compose(RxUtils.doOnFirst(func))
                .subscribe(testSubscriber);

        verify(func, times(1)).call(0);

    }

    @Test
    public void doOnFirst_emptyCallsNothing() {
        Observable.<Integer>empty()
                .compose(RxUtils.doOnFirst(func))
                .subscribe(testSubscriber);

        verify(func, never()).call(anyInt());
    }

    @Test
    public void doOnFirst_neverCallsNothing() {
        Observable.<Integer>never()
                .compose(RxUtils.doOnFirst(func))
                .subscribe(testSubscriber);

        verify(func, never()).call(anyInt());
    }
}
