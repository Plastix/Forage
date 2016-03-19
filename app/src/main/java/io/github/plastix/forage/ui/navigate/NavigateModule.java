package io.github.plastix.forage.ui.navigate;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigateModule {

    private NavigateView view;

    public NavigateModule(NavigateView view) {
        this.view = view;
    }

    @Provides
    public NavigateView provideNavigateView() {
        return view;
    }
}
