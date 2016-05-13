package io.github.plastix.forage.ui;

import android.content.Context;
import android.support.v4.content.Loader;

import javax.inject.Provider;

/**
 * Synchronous Loader used to hold presenter instances outside of the Activity/Fragment lifecycle.
 * Adapted from https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.x1x4xfxc7
 *
 * @param <T> Type of Presenter
 */
public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private T presenter;
    private Provider<T> presenterFactory;

    public PresenterLoader(Context context, Provider<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        // If we already have a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
        } else {
            // Otherwise, force a load
            forceLoad();
        }

    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        // Grab an instance of the presenter from the provider
        presenter = presenterFactory.get();

        // Deliver the presenter
        deliverResult(presenter);

    }

    @Override
    protected void onReset() {
        super.onReset();

        // Clean up presenter resources if we have one
        if (presenter != null) {
            presenter.onDestroyed();
        }
    }
}
