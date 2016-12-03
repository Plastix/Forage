package io.github.plastix.forage.util;

import android.view.View;

public class ViewUtils {

    private ViewUtils() {
    }

    public static void show(View view){
        view.setVisibility(View.VISIBLE);
    }

    public static void hide(View view){
        view.setVisibility(View.GONE);
    }
}
