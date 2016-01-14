package io.github.plastix.forage;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ForageApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        // TODO Move instantiation out of here?
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(getApplicationContext()).build());
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}
