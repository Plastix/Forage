package io.github.plastix.forage.util;

import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AngleUtilsTest {

    @Test
    public void normalize_doesNotModifyNormalAngles() {
        float a = 0;
        float b = 360;
        float c = 180;
        float d = 145.86f;

        assertThat(AngleUtils.normalize(a)).isWithin(0).of(a);
        assertThat(AngleUtils.normalize(b)).isWithin(0).of(a);
        assertThat(AngleUtils.normalize(c)).isWithin(0).of(c);
        assertThat(AngleUtils.normalize(d)).isWithin(0).of(d);
    }

    @Test
    public void normalize_normalizesPositiveAngles() {
        float a = 361;
        float b = 456;
        float c = 720;
        float d = 945.5f;

        assertThat(AngleUtils.normalize(a)).isWithin(0).of(1);
        assertThat(AngleUtils.normalize(b)).isWithin(0).of(96);
        assertThat(AngleUtils.normalize(c)).isWithin(0).of(0);
        assertThat(AngleUtils.normalize(d)).isWithin(0).of(225.5f);
    }

    @Test
    public void normalize_normalizesNegativeAngles() {
        float a = -361;
        float b = -456;
        float c = -720;
        float d = -945.5f;

        assertThat(AngleUtils.normalize(a)).isWithin(0).of(359);
        assertThat(AngleUtils.normalize(b)).isWithin(0).of(264);
        assertThat(AngleUtils.normalize(c)).isWithin(0).of(0);
        assertThat(AngleUtils.normalize(d)).isWithin(0).of(134.5f);
    }

    @Test
    public void getRotationOffset_returnsCorrectRotation() {
        WindowManager windowManager = mock(WindowManager.class);
        Display display = mock(Display.class);
        when(windowManager.getDefaultDisplay()).thenReturn(display);

        when(display.getRotation()).thenReturn(Surface.ROTATION_90);
        assertThat(AngleUtils.getRotationOffset(windowManager)).isEqualTo(90);

        when(display.getRotation()).thenReturn(Surface.ROTATION_180);
        assertThat(AngleUtils.getRotationOffset(windowManager)).isEqualTo(180);

        when(display.getRotation()).thenReturn(Surface.ROTATION_270);
        assertThat(AngleUtils.getRotationOffset(windowManager)).isEqualTo(270);

        when(display.getRotation()).thenReturn(Surface.ROTATION_0);
        assertThat(AngleUtils.getRotationOffset(windowManager)).isEqualTo(0);
    }
}
