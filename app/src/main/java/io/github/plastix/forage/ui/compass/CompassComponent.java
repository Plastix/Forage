package io.github.plastix.forage.ui.compass;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * Dagger component to inject all required dependencies into {@link CompassFragment}.
 */
@Singleton
@Subcomponent(modules = {CompassModule.class})
public interface CompassComponent {
    void injectTo(CompassFragment compassFragment);
}

