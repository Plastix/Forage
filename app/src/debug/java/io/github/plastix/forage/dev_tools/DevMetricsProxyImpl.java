package io.github.plastix.forage.dev_tools;

import android.app.Application;
import android.support.annotation.NonNull;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

public class DevMetricsProxyImpl implements DevMetricsProxy {

    @NonNull
    private final Application application;

    public DevMetricsProxyImpl(@NonNull Application application) {
        this.application = application;
    }

    @Override
    public void apply() {
        AndroidDevMetrics.initWith(application);
    }
}
