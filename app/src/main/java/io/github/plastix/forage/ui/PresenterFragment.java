package io.github.plastix.forage.ui;

import javax.inject.Inject;

/**
 * Base Fragment that holds a Presenter and automatically calls lifecycle callbacks.
 *
 * @param <T> Type of Presenter.
 */
public abstract class PresenterFragment<T extends Presenter> extends BaseFragment {

    protected T presenter;

    /**
     * Automatically set the presenter with Dagger 2 method injection.
     *
     * @param presenter Presenter object from the dependency graph.
     */
    @Inject
    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

}
