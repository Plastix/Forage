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
    public void setView_shouldAttachView() {
        presenter.setView(view);

        assertThat(presenter.view).isSameAs(view);
    }

    private class FakeView implements View {
    }

    private class FakePresenter extends Presenter<FakeView> {
    }
}
