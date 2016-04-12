package io.github.plastix.forage.dev_tools;

import android.app.Application;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugToolsModule {

    @NonNull
    @Singleton
    @Provides
    public DevMetricsProxy provideDevMetricsProxy(@NonNull Application application) {
        return new DevMetricsProxy() {
            @Override
            public void apply() {
                //No Op
            }
        };
    }
}
