package io.github.plastix.forage.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class ActivityUtils {

    public static Intent getApplicationSettingsIntent(Activity activity) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        return intent;
    }
}
