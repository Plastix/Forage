package io.github.plastix.forage.util;

import android.content.res.Resources;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.R;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class StringUtilsTest {

    public static class WithoutRoboelectric {

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

    /**
     * Test input from
     * http://haacked.com/archive/2008/11/11/html-stripping-challenge.aspx
     */
    @RunWith(ForageRoboelectricUnitTestRunner.class)
    public static class WithRoboelectric {

        //
        // Unit tests for stripHtml()
        //

        @Test
        public void stripHtml_emptyString() {
            assertThat(StringUtils.stripHtml("")).isEmpty();
        }

        @Test
        public void stripHtml_normalString() {
            String input = "Regular String";
            assertThat(StringUtils.stripHtml(input)).isEqualTo(input);
        }

        @Test
        public void stripHtml_singleTag() {
            String input = "<foo>";
            assertThat(StringUtils.stripHtml(input)).isEmpty();
        }

        @Test
        public void stripHtml_onlyConsecutiveTags() {
            String input = "<foo><bar><baz />";
            assertThat(StringUtils.stripHtml(input)).isEmpty();
        }

        @Test
        public void stripHtml_textBeforeTag() {
            String input = "Hello<foo>";
            String output = "Hello";
            assertThat(StringUtils.stripHtml(input)).isEqualTo(output);
        }

        @Test
        public void stripHtml_textAfterTag() {
            String input = "<foo>World";
            String output = "World";
            assertThat(StringUtils.stripHtml(input)).isEqualTo(output);
        }

        @Test
        public void stripHtml_textBetweenTags() {
            String input = "<p><foo>World</foo></p>";
            String output = "World";
            assertThat(StringUtils.stripHtml(input)).isEqualTo(output);
        }

        @Test
        public void stripHtml_closingTagInAttrValue() {
            String input = "<foo title=\"/>\" />";
            assertThat(StringUtils.stripHtml(input)).isEmpty();
        }

        @Test
        public void stripHtml_tagClosedByStartTag() {
            String input = "<foo <>Test";
            String output = "Test";
            assertThat(StringUtils.stripHtml(input)).isEqualTo(output);
        }

        // TODO more stripping HTML tests?
    }


}
