package io.github.plastix.forage.ui.base;

import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.forage.ApplicationComponent;

import static com.google.common.truth.Truth.assertThat;

public class BaseActivityTest {

    private FakeBaseActivity baseActivity;

    @Before
    public void beforeEachTest() {
        baseActivity = new FakeBaseActivity();
    }

    @Test
    public void shouldBeInstanceOfAppCompat() {
        assertThat(baseActivity).isInstanceOf(AppCompatActivity.class);
    }

    private class FakeBaseActivity extends BaseActivity {
        @Override
        protected void injectDependencies(ApplicationComponent component) {

        }
    }

}
