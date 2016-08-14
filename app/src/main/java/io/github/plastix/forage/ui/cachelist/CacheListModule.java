package io.github.plastix.forage.ui.cachelist;

import android.support.v4.app.Fragment;

import dagger.Module;
import io.github.plastix.forage.ui.base.FragmentModule;

/**
 * Dagger module that provides dependencies for {@link CacheListFragment} and {@link CacheListActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CacheListModule extends FragmentModule {
    public CacheListModule(Fragment fragment) {
        super(fragment);
    }
}
