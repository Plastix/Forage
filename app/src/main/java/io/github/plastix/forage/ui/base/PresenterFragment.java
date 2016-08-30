package io.github.plastix.forage.ui.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Base Fragment that holds a Presenter and manages its lifecycle using a {@link PresenterLoader}.
 *
 * @param <T> Type of Presenter.
 */
public abstract class PresenterFragment<T extends Presenter<V>, V> extends BaseFragment
        implements LoaderManager.LoaderCallbacks<T> {

    // Internal ID to reference the Loader from the LoaderManager
    private static final int LOADER_ID = 1;

    protected T presenter;

    @Inject
    protected Provider<PresenterLoader<T>> presenterLoaderProvider;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLoader();
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return presenterLoaderProvider.get();
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T presenter) {
        onPresenterProvided(presenter);
    }

    @CallSuper
    protected void onPresenterProvided(T presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        onPresenterDestroyed();
    }

    @CallSuper
    protected void onPresenterDestroyed() {
        presenter = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewAttached(getPresenterView());
    }

    // Override if Fragment does not implement Presenter<View> interface
    protected V getPresenterView() {
        //noinspection unchecked
        return (V) this;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewDetached();
    }
}
