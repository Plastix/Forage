package io.github.plastix.forage.ui.compass;

import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ui.base.ActivityModule;

/**
 * Dagger module that provides dependencies for {@link CompassActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CompassModule extends ActivityModule {
    public CompassModule(AppCompatActivity activity) {
        super(activity);
    }

    @Provides
    public LinearInterpolator provideLinearInterpolator() {
        return new LinearInterpolator();
    }
}
