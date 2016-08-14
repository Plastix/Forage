package io.github.plastix.forage.ui.map;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.forage.ui.base.ActivityModule;

/**
 * Dagger module that provides dependencies for {@link MapActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class MapModule extends ActivityModule {
    public MapModule(AppCompatActivity activity) {
        super(activity);
    }
}
