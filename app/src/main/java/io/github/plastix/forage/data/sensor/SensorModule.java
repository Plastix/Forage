package io.github.plastix.forage.data.sensor;

import android.content.Context;
import android.hardware.SensorManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;

@Module
public class SensorModule {

    @Provides
    @Singleton
    public SensorManager provideSensorManager(@ForApplication Context context) {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
}
