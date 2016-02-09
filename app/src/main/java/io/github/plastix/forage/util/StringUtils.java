package io.github.plastix.forage.util;

import android.location.Location;
import android.text.Html;

public class StringUtils {

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
     * Converts OkApi's String location format to an Android Location format.
     *
     * @param raw String location in "lat|lon"
     * @return Android location object.
     */
    public static Location stringToLocation(String raw) {
        Location location = new Location("");

        final String[] parts = raw.split("\\|");
        location.setLatitude(Double.parseDouble(parts[0]));
        location.setLongitude(Double.parseDouble(parts[1]));

        return location;

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
}
