package io.github.plastix.forage;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.Lazy;
import io.github.plastix.forage.dev_tools.DevMetricsProxy;
import timber.log.Timber;

public class ForageApplication extends Application {

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

        //Use debug tools only in debug builds
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
            Timber.plant(debugTree.get());
            devMetricsProxy.get().apply();
        }
    }

    @NonNull
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this));
    }

}
