package io.github.plastix.forage.util;

import android.content.res.Resources;
import android.text.Html;

import java.util.Locale;

import io.github.plastix.forage.R;

public class StringUtils {

    private StringUtils() {
        // No instantiation
    }

    /**
     * Joins an array of strings into a single string separated by the specified delimiter.
     *
     * @param delimiter String in between joined elements.
     * @param elements  Elements to join together.
     * @return Single joined string.
     */
    public static String join(String delimiter, String[] elements) {
        if (delimiter == null || elements == null) {
            throw new RuntimeException("String join parameters cannot be null!");
        }

        StringBuilder builder = new StringBuilder();
        String separator = "";
        for (String element : elements) {
            builder.append(separator).append(element);
            separator = delimiter;
        }

        return builder.toString();
    }

    /**
     * Parses HTML into a Plain text string
     *
     * @param input String in HTML form
     * @return Plain text string
     * <p/>
     * Based on http://stackoverflow.com/questions/8560045/android-getting-obj-using-textview-settextcharactersequence
     */
    public static String stripHtml(String input) {
        return Html.fromHtml(input).toString()
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();
    }

    /**
     * Capitalizes the first character of the given string.
     *
     * @param input String to capitalize.
     * @return Capitalized string.
     */
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Turns the distance in miles to a human-readable String. For example:
     * <p/>
     * 5.2 -> 5.2 mi
     * 0.3 -> 5280 ft
     *
     * @param resources Resources object to fetch the string resources.
     * @param miles     Miles to convert.
     * @return Human readable string.
     */
    public static String humanReadableImperialDistance(Resources resources, double miles) {
        String distance;
        String units;

        if (miles > 1) {
            distance = String.format(Locale.getDefault(), "%.2f", miles);
            units = resources.getString(R.string.unit_miles);
        } else {
            distance = String.format(Locale.getDefault(), "%.2f", UnitUtils.milesToFeet(miles));
            units = resources.getString(R.string.unit_feet);
        }

        return distance + " " + units;
    }
}
