package io.github.plastix.forage.ui.cachelist;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheListModule {

    private CacheListView cacheListView;

    @Inject
    public CacheListModule(CacheListView cacheListView) {
        this.cacheListView = cacheListView;
    }

    @Provides
    public CacheListView provideCacheListView(){
        return cacheListView;
    }
}
