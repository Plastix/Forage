package io.github.plastix.forage.ui.cachelist;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.forage.data.api.OkApiModule;

@Singleton
@Component(modules = {CacheListModule.class, OkApiModule.class})
public interface CacheListComponent {
    void inject(CacheListFragment cacheListFragment);
}
