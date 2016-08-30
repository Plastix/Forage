package io.github.plastix.forage.ui.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;
import javax.inject.Provider;


/**
 * Base Activity that holds a Presenter and manages its lifecycle using a {@link PresenterLoader}.
 * See {@link PresenterFragment} if you want similar functionality but with a Fragment instead of an
 * activity.
 *
 * @param <T> Type of Presenter.
 */
public abstract class PresenterActivity<T extends Presenter<V>, V> extends BaseActivity
        implements LoaderManager.LoaderCallbacks<T> {

    // Internal ID to reference the Loader from the LoaderManager
    private static final int LOADER_ID = 1;
    protected T presenter;

    @Inject
    protected Provider<PresenterLoader<T>> presenterLoaderProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLoader();
    }

    private void initLoader() {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return presenterLoaderProvider.get();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(getPresenterView());
    }

    // Override if Activity does not implement Presenter<View> interface
    protected V getPresenterView() {
        //noinspection unchecked
        return (V) this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onViewDetached();
    }

}
