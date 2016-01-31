package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.github.plastix.forage.data.local.DatabaseModule;
import io.github.plastix.forage.ui.cachelist.CacheListFragment;

/**
 * Dagger component to inject all required dependencies into {@link CacheDetailFragment}.
 */
@Singleton
@Subcomponent(modules = {CacheDetailModule.class, DatabaseModule.class})
public interface CacheDetailComponent {
    void injectTo(CacheDetailFragment cacheDetailFragment);
}
