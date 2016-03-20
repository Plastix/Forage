package io.github.plastix.forage.ui;

import android.support.annotation.NonNull;

import javax.inject.Inject;


/**
 * Base Activity that holds a Presenter and automatically calls lifecycle callbacks.
 * See {@link PresenterFragment} if you want similar functionality but with a Fragment instead of an
 * activity.
 *
 * @param <T> Type of Presenter.
 */
public abstract class PresenterActivity<T extends Presenter> extends BaseActivity {

    protected T presenter;

    /**
     * Automatically set the presenter with Dagger 2 method injection.
     *
     * @param presenter Presenter object from the dependency graph.
     */
    @Inject
    public void setPresenter(@NonNull T presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
