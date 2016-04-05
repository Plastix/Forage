package io.github.plastix.forage.util;

import android.content.res.Resources;
import android.text.Html;
import android.text.Spannable;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.plastix.forage.R;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Html.class)
public class StringUtilsTest {

    private static Resources resources;

    /**
     * Runs once when the unit test class loads. Set up some objects to be reused by tests.
     */
    @BeforeClass
    public static void onlyOnce() {
        resources = mock(Resources.class);
        when(resources.getString(R.string.unit_feet)).thenReturn("ft");
        when(resources.getString(R.string.unit_miles)).thenReturn("mi");
    }


    //
    // Unit tests for join()
    //

    @Test
    public void join_shouldJoinStrings() {
        String[] input = {"a", "b", "c"};
        String delimiter = ",";
        String result = StringUtils.join(delimiter, input);

        assertThat(result).isEqualTo("a,b,c");
    }

    @Test
    public void join_joinsTwoLongList() {
        String[] input = {"a", "b",};
        String delimiter = ",";
        String result = StringUtils.join(delimiter, input);

        assertThat(result).isEqualTo("a,b");
    }

    @Test
    public void join_joinsOneLongList() {
        String[] input = {"a"};
        String delimiter = ",";
        String result = StringUtils.join(delimiter, input);

        assertThat(result).isEqualTo("a");
    }

    @Test
    public void join_joinsWithEmptyDelimiter() {
        String[] input = {"a", "b", "c"};
        String delimiter = "";
        String result = StringUtils.join(delimiter, input);

        assertThat(result).isEqualTo("abc");
    }

    @Test
    public void join_emptyListReturnsEmptyString() {
        String[] input = {};
        String delimiter = ",";
        String result = StringUtils.join(delimiter, input);

        assertThat(result).isEqualTo("");
    }

    //
    // Unit tests for stripHtml()
    //

    @Test
    public void stripHtml_delegatesToAndroidClass() {
        PowerMockito.mockStatic(Html.class);
        String input = "input string";
        String out = "output string";
        Spannable spannable = mock(Spannable.class);
        when(spannable.toString()).thenReturn(out);
        when(Html.fromHtml(input)).thenReturn(spannable);

        String output = StringUtils.stripHtml(input);

        PowerMockito.verifyStatic();
        Html.fromHtml(input);

        assertThat(out).isEqualTo(output);
    }

    //
    // Unit tests for capitalize()
    //

    @Test
    public void capitalize_regularStringCapitalized() {
        String input = "hello world!";
        String expected = "Hello world!";

        assertThat(StringUtils.capitalize(input)).isEqualTo(expected);
    }

    @Test
    public void capitalize_alreadyCapitalString() {
        String input = "Hello world!";
        String expected = "Hello world!";

        assertThat(StringUtils.capitalize(input)).isEqualTo(expected);
    }

    @Test
    public void capitalize_singleCharString() {
        String input = "a";
        String expected = "A";

        assertThat(StringUtils.capitalize(input)).isEqualTo(expected);

    }

    @Test
    public void capitalize_emptyString() {
        String input = "";

        assertThat(StringUtils.capitalize(input)).isEqualTo(input);
    }

    //
    // Unit tests for humanReadableImperialDistance()
    //


    @Test
    public void humanReadableImperialDistance_showMiles() {
        double dist = 1.5;
        String expected = "1.50 mi";

        assertThat(StringUtils.humanReadableImperialDistance(resources, dist)).isEqualTo(expected);
    }

    @Test
    public void humanReadableImperialDistance_showFeetWhenLessThanOneMile() {
        double dist = 0.5;
        String expected = "2640.00 ft";

        assertThat(StringUtils.humanReadableImperialDistance(resources, dist)).isEqualTo(expected);
    }

    @Test
    public void humanReadableImperialDistance_zeroMiles() {
        double dist = 0;
        String expected = "0.00 ft";

        assertThat(StringUtils.humanReadableImperialDistance(resources, dist)).isEqualTo(expected);
    }

    @Test
    public void humanReadableImperialDistance_oneMileDisplaysInFeet() {
        double dist = 1;
        String expected = "5280.00 ft";

        assertThat(StringUtils.humanReadableImperialDistance(resources, dist)).isEqualTo(expected);
    }
}
