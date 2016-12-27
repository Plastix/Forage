package io.github.plastix.forage;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.Lazy;
import io.github.plastix.forage.data.local.RealmInitWrapper;
import io.github.plastix.forage.dev_tools.DevMetricsProxy;
import timber.log.Timber;

public class ForageApplication extends Application {

    @Inject
    Lazy<RealmInitWrapper> realmProxy;

    @Inject
    Lazy<DevMetricsProxy> devMetricsProxy;

    @Inject
    Lazy<Timber.DebugTree> debugTree;

    private ApplicationComponent component;

    @NonNull
    public static ApplicationComponent getComponent(Context context) {
        return ((ForageApplication) context.getApplicationContext()).getComponent();
    }

    @NonNull
    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = prepareApplicationComponent().build();
        this.component.injectTo(this);

        // Init realm db

        realmProxy.get().init();

        //Use debug tools only in debug builds
        if (BuildConfig.DEBUG) {
            setupDebugTools();
        }
    }

    private void setupDebugTools() {
        Timber.plant(debugTree.get());
        LeakCanary.install(this);
        devMetricsProxy.get().apply();
    }

    @NonNull
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this));
    }


}
