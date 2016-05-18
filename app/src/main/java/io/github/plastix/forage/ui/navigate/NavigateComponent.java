package io.github.plastix.forage.ui.navigate;

import dagger.Subcomponent;
import io.github.plastix.forage.ui.base.ActivityScope;

@ActivityScope
@Subcomponent(
        modules = {
                NavigateModule.class
        }
)
public interface NavigateComponent {
    void injectTo(NavigateActivity activity);
}
