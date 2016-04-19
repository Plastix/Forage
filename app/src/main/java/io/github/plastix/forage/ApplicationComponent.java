package io.github.plastix.forage;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.forage.data.api.HostSelectionInterceptor;
import io.github.plastix.forage.data.api.OkApiModule;
import io.github.plastix.forage.data.api.OkApiService;
import io.github.plastix.forage.data.local.DatabaseModule;
import io.github.plastix.forage.data.location.LocationModule;
import io.github.plastix.forage.data.network.NetworkModule;
import io.github.plastix.forage.data.sensor.SensorModule;
import io.github.plastix.forage.dev_tools.DebugToolsModule;
import io.github.plastix.forage.ui.cachedetail.CacheDetailComponent;
import io.github.plastix.forage.ui.cachedetail.CacheDetailModule;
import io.github.plastix.forage.ui.cachelist.CacheListComponent;
import io.github.plastix.forage.ui.cachelist.CacheListModule;
import io.github.plastix.forage.ui.compass.CompassComponent;
import io.github.plastix.forage.ui.compass.CompassModule;
import io.github.plastix.forage.ui.map.MapComponent;
import io.github.plastix.forage.ui.map.MapModule;
import io.github.plastix.forage.ui.navigate.NavigateComponent;
import io.github.plastix.forage.ui.navigate.NavigateModule;

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
                DebugToolsModule.class,
        }
)
public interface ApplicationComponent {

    // Provide Gson from the real app to the tests without need to inject to the test.
    Gson gson();

    // Provide OkHttp Host Interceptor from the real app to the tests without need to inject to the test.
    HostSelectionInterceptor inteceptor();

    // Provide OkApiService from the real app to the tests without need to inject to the test.
    OkApiService okApiService();

    void injectTo(ForageApplication forageApplication);

    CacheListComponent plus(CacheListModule module);

    CacheDetailComponent plus(CacheDetailModule module);

    CompassComponent plus(CompassModule module);

    MapComponent plus(MapModule module);

    NavigateComponent plus(NavigateModule module);
}
