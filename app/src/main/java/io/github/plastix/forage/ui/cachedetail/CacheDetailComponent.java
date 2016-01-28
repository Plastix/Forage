package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.github.plastix.forage.data.local.DatabaseModule;

@Singleton
@Subcomponent(modules = {CacheDetailModule.class, DatabaseModule.class})
public interface CacheDetailComponent {
    void injectTo(CacheDetailFragment cacheDetailFragment);
}
