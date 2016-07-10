package io.github.plastix.forage.ui.compass;

import android.view.animation.LinearInterpolator;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ui.base.BaseModule;

/**
 * Dagger module that provides dependencies for {@link CompassActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CompassModule extends BaseModule<CompassView> {
    public CompassModule(CompassView view) {
        super(view);
    }

    @Provides
    public LinearInterpolator provideLinearInterpolator() {
        return new LinearInterpolator();
    }
}
