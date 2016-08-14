package io.github.plastix.forage.ui.cachedetail;

import dagger.Subcomponent;
import io.github.plastix.forage.ui.ActivityScope;

/**
 * Dagger component to inject all required dependencies into {@link CacheDetailActivity}.
 */
@ActivityScope
@Subcomponent(
        modules = {
                CacheDetailModule.class
        }
)
public interface CacheDetailComponent {
    void injectTo(CacheDetailActivity cacheDetailActivity);
}
