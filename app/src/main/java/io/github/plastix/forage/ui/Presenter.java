package io.github.plastix.forage.ui;

import javax.inject.Inject;

/**
 * Base Presenter class.
 *
 * @param <T> Generic type of view that the presenter holds.
 */
public abstract class Presenter<T extends View> implements LifecycleCallbacks {

    protected T view;

    /**
     * Automatically set the View with Dagger 2 method injection.
     *
     * @param view View object from the graph.
     */
    @Inject
    public void setView(T view) {
        this.view = view;

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
