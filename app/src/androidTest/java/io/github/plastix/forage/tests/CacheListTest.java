package io.github.plastix.forage.tests;

import android.content.ComponentName;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.rules.NeedsMockWebServer;
import io.github.plastix.forage.screens.CacheListScreen;
import io.github.plastix.forage.ui.cachelist.CacheListActivity;
import io.github.plastix.forage.ui.map.MapActivity;
import io.github.plastix.forage.ui.navigate.NavigateActivity;
import io.github.plastix.forage.util.TestUtils;
import io.github.plastix.forage.util.UiAutomatorUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CacheListTest {

    @Rule
    public IntentsTestRule<CacheListActivity> activityTestRule =
            new IntentsTestRule<>(CacheListActivity.class);

    private CacheListScreen cacheListScreen;
    private UiDevice device;

    private DatabaseInteractor databaseInteractor;


    @Before
    public void beforeEachTest() {
        cacheListScreen = new CacheListScreen();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        databaseInteractor = TestUtils.app().getComponent().databaseInteractor();
    }

    @Test
    public void testToolbarTitle() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        cacheListScreen.shouldDisplayTitle(TestUtils.app().getString(R.string.cachelist_screen_title));
    }


    @Test
    public void testMapButton() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        // Opens MapActivity
        onView(withId(R.id.cachelist_action_map)).perform(click());

        // Verify that an MapActivity intent has been launched
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), MapActivity.class)));
    }

    @Test
    public void testNavigateButton() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());

        // We must click based on the text
        // Android menu items don't the ID of the menu item
        onView(withText(R.string.cachelist_action_navigate)).perform(click());

        // Verify that an Navigate intent has been launched
        intended(hasComponent(new ComponentName(InstrumentationRegistry.getTargetContext(), NavigateActivity.class)));

    }

    @Test
    public void testNoGeocachesDownloaded() {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);
        databaseInteractor.clearGeocaches();

        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore
    @NeedsMockWebServer(setupMethod = "queueNetworkRequest")
    public void shouldDisplayGeocachesFromServer() throws UiObjectNotFoundException {
        UiAutomatorUtils.allowPermissionsIfNeeded(device);

        // Swipe the refresh view
        onView(withId(R.id.cachelist_swiperefresh)).perform(swipeDown());
    }

    public void queueNetworkRequest(final MockWebServer mockWebServer) {
        String jsonResponse = "{\n" +
                "  \"CACHE1\":{\n" +
                "    \"code\":\"CACHE1\",\n" +
                "    \"name\":\"Cache Name 1\",\n" +
                "    \"location\":\"45|45\",\n" +
                "    \"type\":\"Traditional\",\n" +
                "    \"status\":\"Available\",\n" +
                "    \"terrain\":1,\n" +
                "    \"difficulty\":2.5,\n" +
                "    \"size2\":\"micro\",\n" +
                "    \"description\":\"<p>Cache Description 1<\\/p>\"\n" +
                "  },\n" +
                "  \"CACHE2\":{\n" +
                "    \"code\":\"CACHE2\",\n" +
                "    \"name\":\"Cache Name 2\",\n" +
                "    \"location\":\"0|0\",\n" +
                "    \"type\":\"Virtual\",\n" +
                "    \"status\":\"Available\",\n" +
                "    \"terrain\":1,\n" +
                "    \"difficulty\":1,\n" +
                "    \"size2\":\"none\",\n" +
                "    \"description\":\"<p>Cache Description 2<\\/p>\"\n" +
                "  }\n" +
                "}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

    }


}

