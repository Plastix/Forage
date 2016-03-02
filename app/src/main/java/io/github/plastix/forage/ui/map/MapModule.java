package io.github.plastix.forage.ui.map;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides dependencies for {@link MapFragment} and {@link MapActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class MapModule {

    private MapFragView mapView;

    public MapModule(MapFragView mapView) {
        this.mapView = mapView;
    }

    @Provides
    public MapFragView providesMapView() {
        return mapView;
    }
}
