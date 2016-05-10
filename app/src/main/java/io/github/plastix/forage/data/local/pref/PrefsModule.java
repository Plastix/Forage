package io.github.plastix.forage.data.local.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;

@Module
public class PrefsModule {

    // TODO Factor strings out into constants

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ForApplication Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @OAuthUserToken
    @Singleton
    @Provides
    public StringPreference provideOAuthUserToken(SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "OAUTH_USER_TOKEN", null);
    }

    @OAuthUserTokenSecret
    @Singleton
    @Provides
    public StringPreference provideOAuthUserTokenSecret(SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, "OAUTH_USER_TOKEN_SECRET", null);
    }
}
