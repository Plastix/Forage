package io.github.plastix.forage.ui.base;

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

    /**
     * Called when the Presenter instance is being removed. Make sure to release any resources used
     * by the presenter here.
     */
    public abstract void onDestroyed();

    /**
     * Checks if the view is currently attached to the presenter. You should call this method before
     * accessing {@link #view} to avoid NPEs!
     */
    protected boolean isViewAttached() {
        return view != null;
    }


}
