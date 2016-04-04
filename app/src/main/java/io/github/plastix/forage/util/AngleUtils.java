package io.github.plastix.forage.util;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.Surface;
import android.view.WindowManager;

public class AngleUtils {

    private AngleUtils() {
        throw new UnsupportedOperationException("No Instantiation!");
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

    @SuppressLint("SwitchIntDef")
    public static int getRotationOffset(@NonNull WindowManager windowManager) {
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

}
