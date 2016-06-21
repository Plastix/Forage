package io.github.plastix.forage.data.local.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ApplicationScope;

@Module
public class PrefsModule {

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ApplicationScope Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @OAuthUserToken
    @Singleton
    @Provides
    public StringPreference provideOAuthUserToken(SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, SharedPrefConstants.KEY_OAUTH_USER_TOKEN, null);
    }

    @OAuthUserTokenSecret
    @Singleton
    @Provides
    public StringPreference provideOAuthUserTokenSecret(SharedPreferences sharedPreferences) {
        return new StringPreference(sharedPreferences, SharedPrefConstants.KEY_OAUTH_USER_TOKEN_SECRET, null);
    }
}
