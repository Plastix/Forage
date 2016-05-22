package io.github.plastix.forage.ui.cachedetail;

import dagger.Module;
import io.github.plastix.forage.ui.base.BaseModule;

/**
 * Dagger module that provides dependencies for {@link CacheDetailPresenter} and {@link CacheDetailActivity}.
 */
@Module
public class CacheDetailModule extends BaseModule<CacheDetailView> {
    public CacheDetailModule(CacheDetailView view) {
        super(view);
    }
}

