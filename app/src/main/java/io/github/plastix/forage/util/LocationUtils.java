package io.github.plastix.forage.util;

import android.hardware.GeomagneticField;
import android.location.Location;

public class LocationUtils {

    private LocationUtils(){
        // No instantiation
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
}
