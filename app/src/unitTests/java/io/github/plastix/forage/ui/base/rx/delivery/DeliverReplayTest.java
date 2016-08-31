package io.github.plastix.forage.ui.base.rx.delivery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

public class DeliverReplayTest {

    static long TIME_DELAY_MS = 5000L;

    TestScheduler testScheduler;
    TestSubscriber<Integer> testSubscriber;

    @Before
    public void setUp() {
        testScheduler = Schedulers.test();
        testSubscriber = TestSubscriber.create();
    }

    @Test
    public void emitsSingleItemWhenViewIsAttached() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitsTwoItemsWhenViewIsAttached() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitsThreeItemsWhenViewIsAttached() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void noEmissionSingleItemWhenViewIsDetached() {
        Observable<Boolean> view = Observable.just(false);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void noEmissionTwoItemsWhenViewIsDetached() {
        Observable<Boolean> view = Observable.just(false);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void noEmissionThreeItemsWhenViewIsDetached() {
        Observable<Boolean> view = Observable.just(false);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void noEmissionSingleItemWhenViewIsNeverAttached() {
        Observable<Boolean> view = Observable.never();
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void noEmissionTwoItemsWhenViewIsNeverAttached() {
        Observable<Boolean> view = Observable.never();
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void noEmissionThreeItemsWhenViewIsNeverAttached() {
        Observable<Boolean> view = Observable.never();
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void emitsSingleItemWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoValues();
        testSubscriber.assertNotCompleted();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitsTwoItemsWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoValues();
        testSubscriber.assertNotCompleted();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitsThreeItemsWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just(0, 1, 2)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoValues();
        testSubscriber.assertNotCompleted();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertCompleted();
    }

    @Test
    public void emitsErrorWhenViewIsAttached() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>error(new Throwable())
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(Throwable.class);
    }

    @Test
    public void noErrorEmittedWhenViewIsDetached() {
        Observable<Boolean> view = Observable.just(false);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>error(new Throwable())
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void noErrorEmittedWhenViewNeverAttached() {
        Observable<Boolean> view = Observable.never();
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>error(new Throwable())
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
    }

    @Test
    public void emitErrorWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>error(new Throwable())
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(Throwable.class);
    }

    @Test
    public void emitErrorAfterItemsWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just("0", "1", "2", "error")
                .map(Integer::parseInt)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValues(0, 1, 2);
        testSubscriber.assertError(Throwable.class);
    }

    @Test
    public void emitErrorBeforeItemsWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just("error", "0", "1", "2")
                .map(Integer::parseInt)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertNoValues();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoValues();
        testSubscriber.assertError(Throwable.class);
    }

    @Test
    public void emitsErrorInBetweenItemsWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.just("0", "error", "1", "2")
                .map(Integer::parseInt)
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(0);
        testSubscriber.assertError(Throwable.class);
    }

    @Test
    public void emitsCompleteWhenViewIsAttached() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>empty()
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoValues();
        testSubscriber.assertCompleted();
    }

    @Test
    public void noCompleteEmissionWhenViewIsDetached() {
        Observable<Boolean> view = Observable.just(false);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>empty()
                .compose(transformer)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void emitsCompleteWhenViewReattaches() {
        Observable<Boolean> view = Observable.just(true)
                .delay(TIME_DELAY_MS, TimeUnit.MILLISECONDS, testScheduler);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        Observable.<Integer>empty()
                .compose(transformer)
                .subscribeOn(testScheduler)
                .subscribe(testSubscriber);

        testSubscriber.assertNotCompleted();
        testScheduler.advanceTimeBy(TIME_DELAY_MS, TimeUnit.MILLISECONDS);
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
    }

    @Test
    public void noItemsEmittedFromSourceBeforeSubscription() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        final boolean[] emitted = {false}; // Flag required since doOnEach swallows errors
        Observable.just(0, 1, 2)
                .doOnNext(val -> emitted[0] = true)
                .compose(transformer);

        if (emitted[0]) {
            Assert.fail("Source emitted items before downstream observable was subscribed to!");
        }

    }

    @Test
    public void noErrorEmittedFromSourceBeforeSubscription() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        final boolean[] emitted = {false};
        Observable.<Integer>error(new Throwable())
                .doOnError(throwable -> emitted[0] = true)
                .compose(transformer);

        if (emitted[0]) {
            Assert.fail("Source emitted error before downstream observable was subscribed to!");
        }

    }

    @Test
    public void noCompletionEmittedFromSourceBeforeSubscription() {
        Observable<Boolean> view = Observable.just(true);
        Observable.Transformer<Integer, Integer> transformer = new DeliverReplay<>(view);

        final boolean[] emitted = {false};
        Observable.<Integer>empty()
                .doOnCompleted(() -> emitted[0] = true)
                .compose(transformer);

        if (emitted[0]) {
            Assert.fail("Source emitted completion before downstream observable was subscribed to!");
        }

    }
}