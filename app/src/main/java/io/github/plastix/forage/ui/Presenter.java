package io.github.plastix.forage.ui;

/**
 * Base Presenter class.
 *
 * @param <V> Generic type of view that the presenter interacts with.
 */
public abstract class Presenter<V> {

    protected V view;

    public void onViewAttached(V view) {
        this.view = view;
    }

    public void onViewDetached() {
        this.view = null;
    }

    public abstract void onDestroyed();

}
