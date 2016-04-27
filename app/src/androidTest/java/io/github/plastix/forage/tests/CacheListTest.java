package io.github.plastix.forage.tests;

import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.R;
import io.github.plastix.forage.screens.CacheListScreen;
import io.github.plastix.forage.screens.MapScreen;
import io.github.plastix.forage.ui.cachelist.CacheListActivity;
import io.github.plastix.forage.ui.map.MapActivity;
import io.github.plastix.forage.util.TestUtils;
import io.github.plastix.forage.util.UiAutomatorUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CacheListTest {

    @Rule
    public IntentsTestRule<CacheListActivity> activityTestRule =
            new IntentsTestRule<>(CacheListActivity.class);

    private CacheListScreen cacheListScreen;
    private MapScreen mapScreen;
    private UiDevice device;

    @Before
    public void beforeEachTest() {
        cacheListScreen = new CacheListScreen();
        mapScreen = new MapScreen();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testToolbarTitle() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        cacheListScreen.shouldDisplayTitle(TestUtils.app().getString(R.string.cachelist_title));
    }


    @Test
    public void testMapButton() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        // Opens MapActivity
        onView(withId(R.id.cachelist_action_map)).perform(click());

        // Verify that an MapActivity intent has been launched
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), MapActivity.class)));

        // Verify the MapActivity toolbar title
        mapScreen.shouldDisplayTitle(TestUtils.app().getString(R.string.map_title));
    }

    @Test
    public void testNoGeocachesDownloaded() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
    }
}

