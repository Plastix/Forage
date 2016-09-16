package io.github.plastix.forage.ui.login;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

    private LoginPresenter presenter;

    @Mock
    private LoginView loginView;

    @Mock
    private OAuthInteractor oAuthInteractor;

    @Mock
    private NetworkInteractor networkInteractor;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(oAuthInteractor, networkInteractor);
        presenter.onViewAttached(loginView);

    }

    @Test
    public void startOAuth_opensBrowserOnSuccess() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());

        String OAUTH_CALLBACK = "Redirect URL";
        when(oAuthInteractor.retrieveRequestToken()).thenReturn(Single.just(OAUTH_CALLBACK));

        presenter.startOAuth();

        verify(networkInteractor, times(1)).hasInternetConnectionCompletable();
        verify(loginView, times(1)).showLoading();
        verify(loginView, times(1)).openBrowser(OAUTH_CALLBACK);
        verifyNoMoreInteractions(loginView);
    }

    @Test
    public void startOAuth_callsViewIfNoInternet() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(
                Completable.error(new NetworkInteractor.NetworkUnavailableException()));

        presenter.startOAuth();

        verify(networkInteractor, times(1)).hasInternetConnectionCompletable();
        verify(loginView, times(1)).onErrorNoInternet();
        verifyNoMoreInteractions(loginView);

    }

    @Test
    public void startOAuth_errorReturnedToView() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());
        when(oAuthInteractor.retrieveRequestToken()).
                thenReturn(Single.error(new Throwable("Error!")));

        presenter.startOAuth();

        verify(networkInteractor, times(1)).hasInternetConnectionCompletable();
        verify(loginView, times(1)).onErrorRequestToken();
        verify(loginView, times(1)).showLoading();
        verifyNoMoreInteractions(loginView);
    }

    @Test
    public void oauthCallback_onSuccessCallsView() {
        when(oAuthInteractor.retrieveAccessToken(any(Uri.class))).thenReturn(Completable.complete());

        Uri uri = mock(Uri.class);
        presenter.oauthCallback(uri);

        verify(loginView, times(1)).onAuthSuccess();
        verify(loginView, times(1)).showLoading();
        verifyNoMoreInteractions(loginView);
    }

    @Test
    public void oauthCallback_onErrorCallsView() {
        when(oAuthInteractor.retrieveAccessToken(any(Uri.class))).
                thenReturn(Completable.error(new Throwable("Error~")));

        Uri uri = mock(Uri.class);
        presenter.oauthCallback(uri);

        verify(loginView, times(1)).onErrorAccessToken();
        verify(loginView, times(1)).showLoading();
        verifyNoMoreInteractions(loginView);
    }
}

