package io.github.plastix.forage.ui.cachelist;


import dagger.Subcomponent;
import io.github.plastix.forage.ui.FragmentScope;

/**
 * Dagger component to inject all required dependencies into {@link CacheListFragment}.
 */
@FragmentScope
@Subcomponent(
        modules = {
                CacheListModule.class,
        }
)
public interface CacheListComponent {
    void injectTo(CacheListFragment cacheListFragment);
}
