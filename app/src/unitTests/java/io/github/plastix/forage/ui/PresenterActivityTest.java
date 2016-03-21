package io.github.plastix.forage.ui;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class) // Allows for multiple static inner classes with tests
public class PresenterActivityTest {

    /**
     * Mock the Activity so we can test its lifecycle methods.
     */
    public static class MockedActivity {

        private FakePresenterActivity presenterActivity;
        private FakePresenter fakePresenter;

        @Before
        public void beforeEachTest() throws Exception {
            fakePresenter = new FakePresenter();

            presenterActivity = mock(FakePresenterActivity.class);
            presenterActivity.setPresenter(fakePresenter);
        }

        @Test
        public void onResume_shouldCallPresenterOnResume() {
            presenterActivity.onResume();
            verify(presenterActivity).onResume();
        }

        @Test
        public void onPause_shouldCallPresenterOnPause() {
            presenterActivity.onPause();
            verify(presenterActivity).onPause();
        }

        @Test
        public void onStart_shouldCallPresenterOnStart() {
            presenterActivity.onStart();
            verify(presenterActivity).onStart();
        }

        @Test
        public void onStop_shouldCallPresenterOnStop() {
            presenterActivity.onStop();
            verify(presenterActivity).onStop();
        }
    }

    /**
     * Don't mock the Activity so we can test our custom logic.
     * Note that we can't call any Android methods here!
     */
    public static class UnMockedActivity {

        @Test
        public void setPresenter_shouldAttachPresenter() {
            FakePresenterActivity presenterActivity = new FakePresenterActivity();
            FakePresenter fakePresenter = new FakePresenter();

            presenterActivity.setPresenter(fakePresenter);
            assertThat(presenterActivity.presenter).isSameAs(fakePresenter);
        }
    }

    private static class FakePresenter extends Presenter {
    }

    private static class FakePresenterActivity extends PresenterActivity<FakePresenter> {
    }
}
