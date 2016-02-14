package io.github.plastix.forage.util;

public class AngleUtils {

    private AngleUtils() {
        // No instantiation
    }

    /**
     * Normalize an angle so that it is between 0 and 360.
     *
     * @param angle Angle in degrees to normalize
     * @return Normalized angle.
     */
    public static float normalize(final float angle) {
        return (angle >= 0 ? angle : (360 - ((-angle) % 360))) % 360;
    }

}
