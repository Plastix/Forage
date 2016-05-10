package io.github.plastix.forage.data.local.pref;


import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * SharedPreference wrapper based on U2020's preference architecture
 * https://medium.com/google-developer-experts/persist-your-data-elegantly-u2020-way-c50be19acf9#.8j3qerkk3
 */
public class StringPreference {

    private final SharedPreferences preferences;
    private final String key;
    private final String defaultValue;

    public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
        this(preferences, key, null);
    }

    public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key, @Nullable String defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Nullable
    public String get() {
        return preferences.getString(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(@Nullable String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}