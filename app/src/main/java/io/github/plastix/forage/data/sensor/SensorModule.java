package io.github.plastix.forage.data.sensor;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ApplicationScope;

@Module
public class SensorModule {

    @NonNull
    @Provides
    @Singleton
    public static SensorManager provideSensorManager(@NonNull @ApplicationScope Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @NonNull
    @Provides
    @Singleton
    public static WindowManager provideWindowManager(@NonNull @ApplicationScope Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }
}
