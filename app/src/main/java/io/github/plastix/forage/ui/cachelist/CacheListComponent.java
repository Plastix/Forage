package io.github.plastix.forage.ui.cachelist;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.github.plastix.forage.data.api.OkApiModule;
import io.github.plastix.forage.data.local.DatabaseModule;
import io.github.plastix.forage.data.location.LocationModule;

@Singleton
@Subcomponent(modules = {CacheListModule.class, OkApiModule.class, DatabaseModule.class, LocationModule.class})
public interface CacheListComponent {
    void injectTo(CacheListFragment cacheListFragment);
}
