package io.github.plastix.forage.data.local;

import android.app.Application;
import android.support.annotation.NonNull;

public class DatabaseModuleForTest extends DatabaseModule {

    @NonNull
    @Override
    public RealmProxy provideRealmProxy(Application application) {
        return () -> {
        };
    }
}
