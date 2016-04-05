package io.github.plastix.forage.util;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DrawableCompat.class)
public class MenuUtilsTest {

    private MenuItem menuItem;

    @Before
    public void beforeEachTest() {
        menuItem = mock(MenuItem.class);
        PowerMockito.mockStatic(DrawableCompat.class);
    }

    @Test
    public void tintMenuItemIcon_shouldTintIcon() {
        int color = 0;
        Drawable icon = mock(Drawable.class);
        when(menuItem.getIcon()).thenReturn(icon);

        Drawable wrapped = mock(Drawable.class);
        when(DrawableCompat.wrap(icon)).thenReturn(wrapped);

        MenuUtils.tintMenuItemIcon(color, menuItem);

        verify(icon).mutate();
        PowerMockito.verifyStatic();
        DrawableCompat.setTint(wrapped, color);
        verify(menuItem).setIcon(icon);
    }

    @Test
    public void tintMenuItemIcon_noIconDoesNothing() {
        int color = 0;
        when(menuItem.getIcon()).thenReturn(null);

        MenuUtils.tintMenuItemIcon(color, menuItem);

        PowerMockito.verifyStatic(never());
        DrawableCompat.setTint(any(Drawable.class), anyInt());
        verify(menuItem, only()).getIcon();
    }
}
