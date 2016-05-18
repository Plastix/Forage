package io.github.plastix.forage.ui.log;

import dagger.Subcomponent;
import io.github.plastix.forage.ui.ActivityScope;

@ActivityScope
@Subcomponent(modules = {
        LogModule.class
})
public interface LogComponent {
    void injectTo(LogActivity activity);
}
