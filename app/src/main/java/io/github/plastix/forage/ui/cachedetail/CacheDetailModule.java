package io.github.plastix.forage.ui.cachedetail;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides dependencies for {@link CacheDetailPresenter} and {@link CacheDetailActivity}.
 */
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

}

