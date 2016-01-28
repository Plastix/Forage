package io.github.plastix.forage.ui.cachedetail;

import dagger.Module;
import dagger.Provides;

@Module
public class CacheDetailModule {

    private CacheDetailView cacheDetailView;

    public CacheDetailModule(CacheDetailView cacheDetailView) {
        this.cacheDetailView = cacheDetailView;
    }

    @Provides
    public CacheDetailView provideCacheDetailView() {
        return cacheDetailView;
    }

    @Provides
    public CacheDetailFragment provideCacheDetailFragment() {
        return new CacheDetailFragment();
    }

}

