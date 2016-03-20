package io.github.plastix.forage;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

public class ForageApplication extends Application {

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

        this.component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        //Use debug tools only in debug builds
        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
            AndroidDevMetrics.initWith(this);
        }
    }

}
