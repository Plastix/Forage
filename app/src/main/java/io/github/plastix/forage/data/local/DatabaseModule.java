package io.github.plastix.forage.data.local;

import android.content.Context;

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

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    public RealmConfiguration provideDefaultRealmConfig(@ForApplication Context context) {
        return new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
