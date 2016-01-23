package io.github.plastix.forage.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class ActivityUtils {

    public static Intent getApplicationSettingsIntent(Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        return intent;
    }

    public static void setSupportActionBarTitle(Activity activity, int title) {
        ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(activity.getString(title));
        }
    }
}
