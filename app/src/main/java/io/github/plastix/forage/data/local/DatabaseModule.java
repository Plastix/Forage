package io.github.plastix.forage.data.local;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Dagger module to provide a {@link Realm} instance.
 */
@Module
public class DatabaseModule {

    @NonNull
    @Provides
    public Realm provideRealm(@NonNull  RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @NonNull
    @Provides
    @Singleton
    public RealmConfiguration provideDefaultRealmConfig(@NonNull @ForApplication Context context) {
        return new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .name(Realm.DEFAULT_REALM_NAME)
                .build();
    }
}
