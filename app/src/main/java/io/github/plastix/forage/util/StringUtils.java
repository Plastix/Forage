package io.github.plastix.forage.util;

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
     * <p>
     * Based on http://stackoverflow.com/questions/8560045/android-getting-obj-using-textview-settextcharactersequence
     */
    public static String stripHtml(String input) {
        return Html.fromHtml(input).toString()
                .replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim();
    }
}
