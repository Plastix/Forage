package io.github.plastix.forage.data.api.auth;

import java.io.IOException;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.pref.OAuthUserToken;
import io.github.plastix.forage.data.local.pref.OAuthUserTokenSecret;
import io.github.plastix.forage.data.local.pref.StringPreference;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

/**
 * An OKHttp interceptor that signs requests using oauth-signpost.
 * Adapted from https://github.com/pakerfeldt/okhttp-signpost
 * This Interceptor allows oauth signing to be enabled and disabled using a custom request
 * header
 */
public class OAuthSigningInterceptor implements Interceptor {

    public static final String OAUTH_ENABLE_HEADER = "OAuth";

    private final OkHttpOAuthConsumer consumer;

    @Inject
    public OAuthSigningInterceptor(OkHttpOAuthConsumer consumer,
                                   @OAuthUserToken StringPreference userToken,
                                   @OAuthUserTokenSecret StringPreference userTokenSecret) {
        this.consumer = consumer;

        // Set the signing token and secret if we already have one saved in shared prefs
        if (userToken.isSet() && userTokenSecret.isSet()) {
            consumer.setTokenWithSecret(userToken.get(), userTokenSecret.get());
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();


        if (original.header(OAUTH_ENABLE_HEADER) != null) {
            try {
                original = (Request) consumer.sign(original).unwrap();
            } catch (OAuthException e) {
                throw new IOException("Could not sign request", e);
            }
        }
        return chain.proceed(original);
    }

}
