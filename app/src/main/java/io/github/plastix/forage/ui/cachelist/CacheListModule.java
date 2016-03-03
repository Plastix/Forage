package io.github.plastix.forage.ui.cachelist;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides dependencies for {@link CacheListFragment} and {@link CacheListActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CacheListModule {

    private CacheListView cacheListView;

    public CacheListModule(CacheListView cacheListView) {
        this.cacheListView = cacheListView;
    }

    @Provides
    public CacheListView provideCacheListView() {
        return cacheListView;
    }

}
