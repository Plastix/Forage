package io.github.plastix.forage;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.forage.data.api.OkApiModule;
import io.github.plastix.forage.data.local.DatabaseModule;
import io.github.plastix.forage.data.location.LocationModule;
import io.github.plastix.forage.data.network.NetworkModule;
import io.github.plastix.forage.data.sensor.SensorModule;
import io.github.plastix.forage.ui.cachedetail.CacheDetailComponent;
import io.github.plastix.forage.ui.cachedetail.CacheDetailModule;
import io.github.plastix.forage.ui.cachelist.CacheListComponent;
import io.github.plastix.forage.ui.cachelist.CacheListModule;
import io.github.plastix.forage.ui.compass.CompassComponent;
import io.github.plastix.forage.ui.compass.CompassModule;
import io.github.plastix.forage.ui.map.MapComponent;
import io.github.plastix.forage.ui.map.MapModule;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = {
                ApplicationModule.class,
                OkApiModule.class,
                DatabaseModule.class,
                NetworkModule.class,
                LocationModule.class,
                SensorModule.class,
        }
)
public interface ApplicationComponent {

    CacheListComponent plus(CacheListModule module);

    CacheDetailComponent plus(CacheDetailModule module);

    CompassComponent plus(CompassModule module);

    MapComponent plus(MapModule module);
}
