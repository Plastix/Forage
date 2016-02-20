package io.github.plastix.forage.util;

/**
 * Utility class for unit conversions.
 */
public class UnitUtils {

    private UnitUtils() {
        // No instantiation
    }

    public static final double MILES_PER_KILOMETER = 0.621371;
    public static final double KILOMETER_PER_METER = 0.001;
    public static final int FEET_PER_MILE = 5280;

    /**
     * Converts miles to kilometers.
     *
     * @param miles Unit in miles.
     * @return Unit in kilometers.
     */
    public static double milesToKilometer(double miles) {
        return miles / MILES_PER_KILOMETER;
    }

    /**
     * Converts meters to kilometers.
     *
     * @param meter Unit in meters.
     * @return Unit in kilometers.
     */
    public static double metersToKilometers(double meter) {
        return meter * KILOMETER_PER_METER;
    }


    /**
     * Converts meters to miles.
     *
     * @param meters Unit in meters.
     * @return Unit in miles.
     */
    public static double metersToMiles(double meters) {
        return metersToKilometers(meters) * MILES_PER_KILOMETER;
    }

    /**
     * Converts miles to feet.
     *
     * @param miles Unit in miles.
     * @return Unit in feet.
     */
    public static double milesToFeet(double miles) {
        return miles * FEET_PER_MILE;
    }


}
