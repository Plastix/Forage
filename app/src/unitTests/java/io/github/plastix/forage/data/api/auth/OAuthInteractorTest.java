package io.github.plastix.forage.data.api.auth;

import android.net.Uri;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.data.api.ApiConstants;
import io.github.plastix.forage.data.local.pref.StringPreference;
import io.github.plastix.rx1.RxSchedulerRule;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import rx.observers.TestSubscriber;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuthInteractorTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @Mock
    StringPreference userToken;

    @Mock
    StringPreference userTokenSecret;

    @Mock
    OkHttpOAuthProvider okHttpOAuthProvider;

    @Mock
    OkHttpOAuthConsumer okHttpOAuthConsumer;

    OAuthInteractor oAuthInteractor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        oAuthInteractor = new OAuthInteractor(okHttpOAuthProvider, okHttpOAuthConsumer, userToken, userTokenSecret);
    }

    @Test
    public void retrieveRequestToken_success() throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException {
        String token = "some token";
        when(okHttpOAuthProvider.retrieveRequestToken(any(), anyString())).thenReturn(token);

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        oAuthInteractor.retrieveRequestToken()
                .subscribe(testSubscriber);

        testSubscriber.assertValue(token);
        verify(okHttpOAuthProvider, times(1)).retrieveRequestToken(okHttpOAuthConsumer, ApiConstants.OAUTH_CALLBACK);
    }

    @Test
    public void retrieveRequestToken_error() throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException {
        Throwable throwable = new RuntimeException();
        when(okHttpOAuthProvider.retrieveRequestToken(any(), anyString())).thenThrow(throwable);

        TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        oAuthInteractor.retrieveRequestToken()
                .subscribe(testSubscriber);

        testSubscriber.assertError(throwable);
        verify(okHttpOAuthProvider, times(1)).retrieveRequestToken(okHttpOAuthConsumer, ApiConstants.OAUTH_CALLBACK);
    }

    @Test
    public void retrieveAccessToken_success() throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException {
        Uri uri = mock(Uri.class);
        String verifier = "some verifier";
        when(uri.getQueryParameter(OAuth.OAUTH_VERIFIER)).thenReturn(verifier);

        String userTokenString = "some token";
        when(okHttpOAuthConsumer.getToken()).thenReturn(userTokenString);

        String userTokenSecretString = "some secret token";
        when(okHttpOAuthConsumer.getTokenSecret()).thenReturn(userTokenSecretString);


        TestSubscriber testSubscriber = new TestSubscriber();
        oAuthInteractor.retrieveAccessToken(uri)
                .subscribe(testSubscriber);

        testSubscriber.assertCompleted();

        verify(okHttpOAuthProvider, times(1)).retrieveAccessToken(okHttpOAuthConsumer, verifier);
        verify(userToken, times(1)).set(userTokenString);
        verify(userTokenSecret, times(1)).set(userTokenSecretString);
    }

    @Test
    public void retrieveAccessToken_error() throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthNotAuthorizedException, OAuthMessageSignerException {
        Uri uri = mock(Uri.class);
        when(uri.getQueryParameter(OAuth.OAUTH_VERIFIER)).thenReturn(null);

        TestSubscriber testSubscriber = new TestSubscriber();
        oAuthInteractor.retrieveAccessToken(uri)
                .subscribe(testSubscriber);

        // We don't really care what the error is just that we have one
        // Hacky way to see if there is an error (terminal event) but not success (completion)
        testSubscriber.assertTerminalEvent();
        testSubscriber.assertNotCompleted();
    }

    @Test
    public void hasSavedTokens_returnsTrueWhenHasTokens() {
        when(userToken.isSet()).thenReturn(true);
        when(userTokenSecret.isSet()).thenReturn(true);

        assertThat(oAuthInteractor.hasSavedOAuthTokens()).isTrue();
    }

    @Test
    public void hasSavedTokens_returnsFalseWithOneToken() {
        when(userToken.isSet()).thenReturn(false);
        when(userTokenSecret.isSet()).thenReturn(true);

        assertThat(oAuthInteractor.hasSavedOAuthTokens()).isFalse();
    }

    @Test
    public void hasSavedTokens_returnsFalseWithOneToken2() {
        when(userToken.isSet()).thenReturn(true);
        when(userTokenSecret.isSet()).thenReturn(false);

        assertThat(oAuthInteractor.hasSavedOAuthTokens()).isFalse();
    }

    @Test
    public void hasSavedTokens_returnsFalseWithNeitherToken() {
        when(userToken.isSet()).thenReturn(false);
        when(userTokenSecret.isSet()).thenReturn(false);

        assertThat(oAuthInteractor.hasSavedOAuthTokens()).isFalse();
    }

    @Test
    public void clearSavedTokens_deletesSecretsFromSharedPref() {
        oAuthInteractor.clearSavedOAuthTokens();

        verify(userToken, times(1)).delete();
        verify(userTokenSecret, times(1)).delete();
    }
}
