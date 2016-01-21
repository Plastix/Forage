package io.github.plastix.forage.data.local;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Dagger module to provide a {@link Realm} instance.
 */
@Module
public class DatabaseModule {

    @Provides
    public Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}
