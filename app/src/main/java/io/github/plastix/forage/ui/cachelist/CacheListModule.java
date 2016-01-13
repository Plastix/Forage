package io.github.plastix.forage.ui.cachelist;

import dagger.Module;
import dagger.Provides;

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
