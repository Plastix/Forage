package io.github.plastix.forage.ui.cachelist;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.github.plastix.forage.data.api.OkApiModule;
import io.github.plastix.forage.data.local.DatabaseModule;

@Singleton
@Subcomponent(modules = {CacheListModule.class, OkApiModule.class, DatabaseModule.class})
public interface CacheListComponent {
    void injectTo(CacheListFragment cacheListFragment);
}
