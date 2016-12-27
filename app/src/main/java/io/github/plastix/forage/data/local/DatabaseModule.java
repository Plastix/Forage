package io.github.plastix.forage.data.local;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Dagger module to provide a {@link Realm} instance.
 */
@Module
public class DatabaseModule {

    @NonNull
    @Provides
    public static Realm provideRealm(@NonNull RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @NonNull
    @Provides
    @Singleton
    public static RealmConfiguration provideDefaultRealmConfig() {
        return new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(Realm.DEFAULT_REALM_NAME)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    public RealmInitWrapper provideRealmProxy(Application application) {
        return new RealmInitWrapperImpl(application);
    }
}
