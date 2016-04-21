package io.github.plastix.forage;

import android.app.Application;
import android.support.annotation.NonNull;

import io.github.plastix.forage.dev_tools.DebugToolsModule;
import io.github.plastix.forage.dev_tools.DevMetricsProxy;

public class ForageFunctionalTestApplication extends ForageApplication {

    @NonNull
    @Override
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return super.prepareApplicationComponent()
                .debugToolsModule(new DebugToolsModule() {
                    @NonNull
                    @Override
                    public DevMetricsProxy provideDevMetricsProxy(@NonNull Application application) {
                        return new DevMetricsProxy() {
                            @Override
                            public void apply() {
                                //No Op
                            }
                        };
                    }
                });
    }
}
