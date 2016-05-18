package io.github.plastix.forage.ui.compass;

import dagger.Subcomponent;
import io.github.plastix.forage.ui.base.ActivityScope;

/**
 * Dagger component to inject all required dependencies into {@link CompassFragment}.
 */
@ActivityScope
@Subcomponent(
        modules = {
                CompassModule.class,
        }
)
public interface CompassComponent {
    void injectTo(CompassFragment compassFragment);
}

