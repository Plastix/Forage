package io.github.plastix.forage.ui.navigate;

import dagger.Module;
import io.github.plastix.forage.ui.base.BaseModule;

@Module
public class NavigateModule extends BaseModule<NavigateView> {
    public NavigateModule(NavigateView view) {
        super(view);
    }
}
