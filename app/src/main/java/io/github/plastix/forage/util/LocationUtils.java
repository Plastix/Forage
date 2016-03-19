package io.github.plastix.forage.util;

import android.hardware.GeomagneticField;
import android.location.Location;

public class LocationUtils {

    private static double MIN_LATITUDE = -90;
    private static double MAX_LATITUDE = 90;
    private static double MIN_LONGITUDE = -180;
    private static double MAX_LONGITUDE = 180;

    private LocationUtils() {
        // No instantiation
    }

    /**
     * Helper method for creating a new Location object with the specified latitude and longitude.
     * Android's Location class doesn't have a nice constructor or builder.
     *
     * @param lat Latitude of location.
     * @param lon Longitude of location.
     * @return New location object.
     */
    public static Location buildLocation(double lat, double lon) {
        Location location = new Location(""); // Blank location provider name
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }

    /**
     * Gets the magnetic declination at the specified location.
     *
     * @param location Current location.
     * @return The declination of the horizontal component of the magnetic
     * field from true north, in degrees (i.e. positive means the
     * magnetic field is rotated east that much from true north).
     */
    public static double getMagneticDeclination(Location location) {
        GeomagneticField geoField = new GeomagneticField(
                (float) location.getLatitude(),
                (float) location.getLongitude(),
                (float) location.getAltitude(),
                System.currentTimeMillis()
        );

        return geoField.getDeclination();
    }

    /**
     * String overloaded version of {@link #isValidLatitude(double)}.
     *
     * @param latitude String Latitude
     * @return True if valid, else false.
     */
    public static boolean isValidLatitude(String latitude) {
        try {
            return isValidLatitude(Double.valueOf(latitude));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the specified double is a valid latitude.
     *
     * @param latitude Latitude as double.
     * @return True if valid, else false.
     */
    public static boolean isValidLatitude(double latitude) {
        return latitude < MAX_LATITUDE && MIN_LATITUDE < latitude;
    }

    /**
     * String overloaded version of {@link #isValidLongitude(double)}.
     *
     * @param longitude String Latitude
     * @return True if valid, else false.
     */
    public static boolean isValidLongitude(String longitude) {
        try {
            return isValidLongitude(Double.valueOf(longitude));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether the specified double is a valid longitude.
     *
     * @param longitude Longitude as double.
     * @return True if valid, else false.
     */
    public static boolean isValidLongitude(double longitude) {
        return longitude < MAX_LONGITUDE && MIN_LONGITUDE < longitude;
    }
}
