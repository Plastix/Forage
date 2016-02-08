package io.github.plastix.forage.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class ActivityUtils {

    /**
     * Returns an intent for launching the application settings for the app with the specified activity.
     *
     * @param activity Activity of app to launch settings for.
     * @return Intent object.
     */
    public static Intent getApplicationSettingsIntent(Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        return intent;
    }

    /**
     * Overloaded version of {@link #setSupportActionBarTitle(Activity, String)}.
     *
     * @param activity Activity to set action bar on.
     * @param titleID  String ID of title.
     */
    public static void setSupportActionBarTitle(Activity activity, @StringRes int titleID) {
        setSupportActionBarTitle(activity, activity.getString(titleID));
    }

    /**
     * Sets the support action bar title for the specified activity.
     *
     * @param activity Activity to set action bar on. Must be a {@link AppCompatActivity}.
     * @param title    String title to set.
     */
    public static void setSupportActionBarTitle(Activity activity, String title) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}
