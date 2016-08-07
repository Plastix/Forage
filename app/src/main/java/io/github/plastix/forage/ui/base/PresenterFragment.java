package io.github.plastix.forage.ui.base;

import android.os.Bundle;
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
public abstract class PresenterFragment<T extends Presenter<V>, V> extends BaseFragment {

    // Internal ID to reference the Loader from the LoaderManager
    private static final int LOADER_ID = 1;

    protected T presenter;

    @Inject
    protected Provider<PresenterLoader<T>> presenterLoaderProvider;

    // Boolean flag to avoid delivering the Presenter twice. Calling initLoader in onActivityCreated means
    // onLoadFinished will be called twice during configuration change.
    private boolean delivered = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initLoader();
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<T>() {
            @Override
            public Loader<T> onCreateLoader(int id, Bundle args) {
                return presenterLoaderProvider.get();
            }

            @Override
            public void onLoadFinished(Loader<T> loader, T data) {
                if (!delivered) {
                    presenter = data;
                    delivered = true;
                    onPresenterPrepared(presenter);
                }
            }

            @Override
            public void onLoaderReset(Loader<T> loader) {
                presenter = null;
                onPresenterDestroyed();
            }
        });
    }

    protected void onPresenterPrepared(T presenter) {
        // hook for subclasses
    }

    protected void onPresenterDestroyed() {
        // hook for subclasses
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    /**
     * Subclasses must call this method to inject dependencies from Dagger 2.
     */
    protected abstract void injectDependencies();

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
        presenter.onViewDetached();
        super.onPause();
    }
}
