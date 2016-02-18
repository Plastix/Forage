package io.github.plastix.forage.ui.cachelist;


import dagger.Subcomponent;
import io.github.plastix.forage.ui.ActivityScope;

/**
 * Dagger component to inject all required dependencies into {@link CacheListFragment}.
 */
@ActivityScope
@Subcomponent(
        modules = {
                CacheListModule.class,
        }
)
public interface CacheListComponent {
    void injectTo(CacheListFragment cacheListFragment);
}
