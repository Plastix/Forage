package io.github.plastix.forage.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Utility class for handling Marshmallow runtime Permissions
 */
public class PermissionUtils {

    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    /**
     * Checks whether the application has permission for every permission in the list,
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String perm : permissions) {
            if (!hasPermission(context, perm)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether the application has access to a specific permission.
     */
    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        // If the request is canceled the results array will be empty
        if (grantResults.length == 0) {
            return false;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
