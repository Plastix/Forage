package io.github.plastix.forage.ui;

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

        assertThat(presenter.view).isSameAs(view);
    }

    @Test
    public void onViewDetatched_shouldClearView() {
        presenter.onViewAttached(view);
        presenter.onViewDetached();

        assertThat(presenter.view).isNull();
    }

    private class FakeView {
    }

    private class FakePresenter extends Presenter<FakeView> {
        @Override
        public void onDestroyed() {
            // No op
        }
    }
}
