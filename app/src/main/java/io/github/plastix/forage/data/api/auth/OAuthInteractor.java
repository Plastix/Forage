package io.github.plastix.forage.data.api.auth;

import android.net.Uri;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.ApiConstants;
import io.github.plastix.forage.data.local.pref.OAuthUserToken;
import io.github.plastix.forage.data.local.pref.OAuthUserTokenSecret;
import io.github.plastix.forage.data.local.pref.StringPreference;
import oauth.signpost.OAuth;
import rx.Completable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthProvider;

public class OAuthInteractor {

    private OkHttpOAuthProvider okHttpOAuthProvider;
    private OkHttpOAuthConsumer okHttpOAuthConsumer;
    private StringPreference userToken;
    private StringPreference userTokenSecret;


    @Inject
    public OAuthInteractor(OkHttpOAuthProvider okHttpOAuthProvider,
                           OkHttpOAuthConsumer okHttpOAuthConsumer,
                           @OAuthUserToken StringPreference userToken,
                           @OAuthUserTokenSecret StringPreference userTokenSecret) {
        this.okHttpOAuthProvider = okHttpOAuthProvider;
        this.okHttpOAuthConsumer = okHttpOAuthConsumer;
        this.userToken = userToken;
        this.userTokenSecret = userTokenSecret;
    }

    public Single<String> retrieveRequestToken() {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // Makes a blocking HTTP request
                return okHttpOAuthProvider.retrieveRequestToken(okHttpOAuthConsumer, ApiConstants.OAUTH_CALLBACK);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable retrieveAccessToken(final Uri callback) {
        return Completable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final String verifier = callback.getQueryParameter(OAuth.OAUTH_VERIFIER);

                // Denied OAuth authorization requests will not have an OAuth verifier
                // Throw an error and let the subscriber handle it downstream
                if (verifier == null) {
                    throw new Exception("OAuth Request denied!");
                }

                // Makes a blocking HTTP request
                okHttpOAuthProvider.retrieveAccessToken(okHttpOAuthConsumer, verifier);

                return null;
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                // okHttpOAuthProvider.retrieveAccessToken sets the token and secret on the
                // OAuthConsumer so all we have to do is store the values in shared preferences
                userToken.set(okHttpOAuthConsumer.getToken());
                userTokenSecret.set(okHttpOAuthConsumer.getTokenSecret());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean hasSavedOAuthTokens() {
        return userToken.isSet() & userTokenSecret.isSet();
    }

    public void clearSavedOAuthTokens(){
        userToken.delete();
        userTokenSecret.delete();
    }


}
