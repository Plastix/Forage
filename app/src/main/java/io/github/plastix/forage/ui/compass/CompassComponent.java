package io.github.plastix.forage.ui.compass;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.github.plastix.forage.data.location.LocationModule;
import io.github.plastix.forage.data.sensor.SensorModule;

/**
 * Dagger component to inject all required dependencies into {@link CompassFragment}.
 */
@Singleton
@Subcomponent(modules = {
        CompassModule.class,
        SensorModule.class,
        LocationModule.class
    }
)
public interface CompassComponent {
    void injectTo(CompassFragment compassFragment);
}

