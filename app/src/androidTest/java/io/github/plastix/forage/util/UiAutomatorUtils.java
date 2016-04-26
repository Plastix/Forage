package io.github.plastix.forage.util;

import android.os.Build;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

public class UiAutomatorUtils {

    public static final String TEXT_ALLOW = "Allow";
    public static final String TEXT_DENY = "Deny";
    public static final String TEXT_NEVER_ASK_AGAIN = "Never ask again";

    public UiAutomatorUtils() {
        throw new IllegalStateException("No instantiation!");
    }

    public static void allowPermissionsIfNeeded(UiDevice device) {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = device.findObject(new UiSelector().text(TEXT_ALLOW));

            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException ignored) {
                }
            }
        }
    }

    public static void denyPermissions(UiDevice device) {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject denyPermissions = device.findObject(new UiSelector().text(TEXT_DENY));

            if (denyPermissions.exists()) {
                try {
                    denyPermissions.click();
                } catch (UiObjectNotFoundException ignored) {
                }
            }
        }
    }

}
