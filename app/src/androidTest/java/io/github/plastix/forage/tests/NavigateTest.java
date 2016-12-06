package io.github.plastix.forage.tests;

import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.espresso.ViewMatchers;
import io.github.plastix.forage.screens.NavigateScreen;
import io.github.plastix.forage.ui.compass.CompassActivity;
import io.github.plastix.forage.ui.navigate.NavigateActivity;
import io.github.plastix.forage.util.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        navigateScreen.shouldDisplayTitle(app.getString(R.string.navigate_screen_title));
    }

    @Test
    public void typeInvalidLatitude() {
        onView(withId(R.id.navigate_latitude)).perform(typeText("hello"));
        onView(withId(R.id.navigate_longitude)).perform(typeText("0"));

        onView(withId(R.id.navigate_button)).perform(click());

        String error = app.getString(R.string.navigate_invalid_latitude);
        onView(withId(R.id.navigate_latitude_text_input_layout))
                .check(matches(ViewMatchers.hasTextInputLayoutErrorText(error)));

    }

    @Test
    public void typeInvalidLongitude() {
        onView(withId(R.id.navigate_longitude)).perform(typeText("hello"));
        onView(withId(R.id.navigate_latitude)).perform(typeText("0"));

        onView(withId(R.id.navigate_button)).perform(click());

        String error = app.getString(R.string.navigate_invalid_longitude);
        onView(withId(R.id.navigate_longitude_text_input_layout))
                .check(matches(ViewMatchers.hasTextInputLayoutErrorText(error)));

    }

    @Test
    public void openCompass() {
        onView(withId(R.id.navigate_longitude)).perform(typeText("0"));
        onView(withId(R.id.navigate_latitude)).perform(typeText("0"));

        onView(withId(R.id.navigate_button)).perform(click());

        // Verify that an CompassActivity intent has been launched
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), CompassActivity.class)));
    }
}
