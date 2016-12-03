package io.github.plastix.forage.util;

import android.support.annotation.NonNull;
import android.view.View;

public class ViewUtils {

    private ViewUtils() {
    }

    public static void show(@NonNull View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hide(@NonNull View view) {
        view.setVisibility(View.GONE);
    }

    public static void invis(@NonNull View view) {
        view.setVisibility(View.INVISIBLE);
    }
}
