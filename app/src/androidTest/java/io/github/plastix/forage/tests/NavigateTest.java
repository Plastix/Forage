package io.github.plastix.forage.tests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.screens.NavigateScreen;
import io.github.plastix.forage.ui.navigate.NavigateActivity;
import io.github.plastix.forage.util.TestUtils;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NavigateTest {

    @Rule
    public IntentsTestRule<NavigateActivity> intentsTestRule = new IntentsTestRule<>(NavigateActivity.class);

    private NavigateScreen navigateScreen;
    private ForageApplication app;

    @Before
    public void beforeEachTest() {
        app = TestUtils.app();
        navigateScreen = new NavigateScreen();
    }

    @Test
    public void testToolbarTitle() {
        navigateScreen.shouldDisplayTitle(app.getString(R.string.navigate_title));
    }
}
