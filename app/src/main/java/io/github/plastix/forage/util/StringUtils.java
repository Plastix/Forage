package io.github.plastix.forage.util;

public class StringUtils {

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
}
