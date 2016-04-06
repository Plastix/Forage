package io.github.plastix.forage.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.plastix.forage.data.ObservableManager;
import io.github.plastix.forage.util.RxUtils;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.BooleanSubscription;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RxUtils.class)
public class ReactivePresenterTest {

    private FakePresenter presenter;
    private FakeView view;
    private ObservableManager observableManager;

    @Before
    public void beforeEachTest() {
        view = mock(FakeView.class);
        presenter = spy(new FakePresenter());
        presenter.setView(view);

        observableManager = mock(ObservableManager.class);
        presenter.setCache(observableManager);
        PowerMockito.mockStatic(RxUtils.class);
    }


    @Test
    public void subscriptionShouldBeEmpty() {
        assertThat(presenter.subscription).isInstanceOf(BooleanSubscription.class);

        assertThat(presenter.subscription.isUnsubscribed()).isFalse();
    }

    @Test
    public void setCache_shouldSetObservableManager() {
        assertThat(presenter.cache).isEqualTo(observableManager);
    }

    @Test
    public void unsubscribe_callsRxUtils() {
        presenter.unsubscribe();
        PowerMockito.verifyStatic();
        RxUtils.safeUnsubscribe(presenter.subscription);
    }

    @Test
    public void onPause_callsUnsubscribe() {
        presenter.onPause();
        PowerMockito.verifyStatic();
        RxUtils.safeUnsubscribe(presenter.subscription);
    }

    @Test
    public void subscribe_cachesObservableIfNeeded() {
        // Set the presenter request manually
        // Normally onResume() does this but we don't want to rely on that method in the test
        presenter.request = spy(presenter.buildObservable());

        when(observableManager.isStored(presenter.getObservableId())).thenReturn(false);
        presenter.subscribe();

        verify(observableManager, times(1)).isStored(presenter.getObservableId());
        verify(observableManager, times(1)).storeObservable(anyString(), any(Observable.class));
        verify(presenter.request, times(1)).subscribe();
    }

    @Test
    public void subscribe_doesNotStoreObservable() {
        presenter.request = spy(presenter.buildObservable());

        when(observableManager.isStored(presenter.getObservableId())).thenReturn(true);
        presenter.subscribe();

        verify(observableManager, times(1)).isStored(presenter.getObservableId());
        verify(observableManager, never()).storeObservable(anyString(), any(Observable.class));
        verify(presenter.request, times(1)).subscribe();
    }

    @Test
    public void onResume_reattachesToCachedObservable() {
        String id = presenter.getObservableId();
        when(observableManager.isStored(id)).thenReturn(true);
        when(observableManager.getObservable(id)).thenReturn(Observable.empty());

        presenter.onResume();

        verify(observableManager, atLeastOnce()).isStored(id);
        verify(observableManager, times(1)).getObservable(id);
        verify(presenter, times(1)).subscribe();
        verify(presenter, times(1)).onAttachObservable();
        verify(presenter, never()).buildObservable();

    }

    @Test
    public void oonResume_buildsNewObservable() {
        String id = presenter.getObservableId();
        when(observableManager.isStored(id)).thenReturn(false);

        presenter.onResume();

        verify(observableManager, times(1)).isStored(id);
        verify(observableManager, never()).getObservable(id);
        verify(presenter, never()).subscribe();
        verify(presenter, never()).onAttachObservable();
        verify(presenter, times(1)).buildObservable();

    }

    //
    // Fake class stubs
    //

    private class FakeView implements View {
    }

    private class FakePresenter extends ReactivePresenter<FakeView, Object> {

        @Override
        protected String getObservableId() {
            return "id";
        }

        @Override
        protected Subscriber<Object> buildSubscription() {
            return new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {

                }
            };
        }

        @Override
        protected Observable<Object> buildObservable() {
            return Observable.empty();
        }
    }
}
