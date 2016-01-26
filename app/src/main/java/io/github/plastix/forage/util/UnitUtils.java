package io.github.plastix.forage.util;

public class UnitUtils {

    public static final double MILES_PER_KILOMETER = 0.621371;

    /**
     * Converts miles to kilometers
     **/
    public static double milesToKilometer(Double miles) {
        return miles / MILES_PER_KILOMETER;
    }


}
