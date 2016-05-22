package io.github.plastix.forage.ui.base;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class BaseModule<V> {

    private V view;

    public BaseModule(V view) {
        this.view = view;
    }

    @Provides
    public V providesView() {
        return view;
    }
}
