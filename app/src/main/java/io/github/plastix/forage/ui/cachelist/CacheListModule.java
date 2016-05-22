package io.github.plastix.forage.ui.cachelist;

import dagger.Module;
import io.github.plastix.forage.ui.base.BaseModule;

/**
 * Dagger module that provides dependencies for {@link CacheListFragment} and {@link CacheListActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CacheListModule extends BaseModule<CacheListView> {
    public CacheListModule(CacheListView view) {
        super(view);
    }
}
