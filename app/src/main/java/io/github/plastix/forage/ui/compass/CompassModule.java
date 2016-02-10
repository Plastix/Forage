package io.github.plastix.forage.ui.compass;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides dependencies for {@link CompassFragment} and {@link CompassActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CompassModule {

    private CompassView compassView;

    public CompassModule(CompassView cacheListView) {
        this.compassView = cacheListView;
    }

    @Provides
    public CompassView provideCompassView() {
        return compassView;
    }
}
