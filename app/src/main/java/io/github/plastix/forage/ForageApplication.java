package io.github.plastix.forage;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ForageApplication extends Application {

    @Inject
    RealmConfiguration defaultConfig;
    private ApplicationComponent component;

    public static ApplicationComponent getComponent(Context context) {
        return ((ForageApplication) context.getApplicationContext()).getComponent();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        // Inject requested dependencies
        component.injectTo(this);

        LeakCanary.install(this);

        setRealmDefaultConfig();
    }

    private void setRealmDefaultConfig() {
        Realm.setDefaultConfiguration(defaultConfig);
    }
}
