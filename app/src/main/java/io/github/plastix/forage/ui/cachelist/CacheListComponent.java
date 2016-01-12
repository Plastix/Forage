package io.github.plastix.forage.ui.cachelist;

import dagger.Component;

@Component(modules = CacheListModule.class)
public interface CacheListComponent {
    void inject(CacheListView cacheListView);
}
