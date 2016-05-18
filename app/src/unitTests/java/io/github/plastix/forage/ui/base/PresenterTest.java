package io.github.plastix.forage.ui.base;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class PresenterTest {

    private FakePresenter presenter;
    private FakeView view;

    @Before
    public void beforeTest() {
        presenter = new FakePresenter();
        view = new FakeView();
    }

    @Test
    public void onViewAttached_shouldSetView() {
        presenter.onViewAttached(view);

        assertThat(presenter.getView()).isSameAs(view);
    }

    @Test
    public void onViewDetached_shouldClearView() {
        presenter.onViewAttached(view);
        presenter.onViewDetached();

        assertThat(presenter.getView()).isNull();
    }

    private class FakeView {
    }

    private class FakePresenter extends Presenter<FakeView> {

        @Override
        public void onDestroyed() {
            // No op
        }

        /**
         * Getter for tests
         */
        public FakeView getView() {
            return view;
        }
    }
}
