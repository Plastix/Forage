package io.github.plastix.forage.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

public class MenuUtils {

    private MenuUtils() {
        throw new UnsupportedOperationException("No Instantiation!");
    }

    /**
     * Tints the color of the icon of the specified MenuItem.
     * See http://stackoverflow.com/questions/28219178/toolbar-icon-tinting-on-android
     *
     * @param color Color to tint
     * @param item  MenuItem to tint.
     */
    public static void tintMenuItemIcon(@ColorInt int color, MenuItem item) {
        final Drawable drawable = item.getIcon();
        if (drawable != null) {
            final Drawable wrapped = DrawableCompat.wrap(drawable);
            drawable.mutate();

            DrawableCompat.setTint(wrapped, color);
            item.setIcon(drawable);
        }
    }
}
