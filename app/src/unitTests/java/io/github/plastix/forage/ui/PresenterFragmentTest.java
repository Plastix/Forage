package io.github.plastix.forage.ui;

import android.annotation.SuppressLint;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class) // Allows for multiple static inner classes with tests
public class PresenterFragmentTest {

    /**
     * Mock the Fragment so we can test its lifecycle methods.
     */
    public static class MockedFragment {

        private FakePresenterFragment presenterFragment;
        private FakePresenter fakePresenter;

        @Before
        public void beforeEachTest() throws Exception {
            fakePresenter = new FakePresenter();

            presenterFragment = mock(FakePresenterFragment.class);
            presenterFragment.setPresenter(fakePresenter);
        }

        @Test
        public void onResume_shouldCallPresenterOnResume() {
            presenterFragment.onResume();
            verify(presenterFragment).onResume();
        }

        @Test
        public void onPause_shouldCallPresenterOnPause() {
            presenterFragment.onPause();
            verify(presenterFragment).onPause();
        }

        @Test
        public void onStart_shouldCallPresenterOnStart() {
            presenterFragment.onStart();
            verify(presenterFragment).onStart();
        }

        @Test
        public void onStop_shouldCallPresenterOnStop() {
            presenterFragment.onStop();
            verify(presenterFragment).onStop();
        }
    }

    /**
     * Don't mock the Fragment so we can test our custom logic.
     * Note that we can't call any Android methods here!
     */
    public static class UnMockedFragment {

        @Test
        public void setPresenter_shouldAttachPresenter() {
            FakePresenterFragment presenterFragment = new FakePresenterFragment();
            FakePresenter fakePresenter = new FakePresenter();

            presenterFragment.setPresenter(fakePresenter);
            assertThat(presenterFragment.presenter).isSameAs(fakePresenter);
        }
    }

    private static class FakePresenter extends Presenter {
    }

    @SuppressLint("ValidFragment")
    private static class FakePresenterFragment extends PresenterFragment<FakePresenter> {
        @Override
        protected int getFragmentLayout() {
            return 0;
        }
    }
}