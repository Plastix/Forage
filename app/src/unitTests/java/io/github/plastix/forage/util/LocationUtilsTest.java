package io.github.plastix.forage.util;

import android.location.Location;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * TODO: Test {@link LocationUtils#buildLocation(double, double)} and {@link LocationUtils#getMagneticDeclination(Location)}
 */
public class LocationUtilsTest {

    @Test
    public void isValidLatitude_doubleInput() {
        assertThat(LocationUtils.isValidLatitude(90)).isTrue();
        assertThat(LocationUtils.isValidLatitude(-90)).isTrue();
        assertThat(LocationUtils.isValidLatitude(0)).isTrue();
        assertThat(LocationUtils.isValidLatitude(-91)).isFalse();
        assertThat(LocationUtils.isValidLatitude(91)).isFalse();
    }

    @Test
    public void isValidLatitude_stringInput() {
        assertThat(LocationUtils.isValidLatitude("90")).isTrue();
        assertThat(LocationUtils.isValidLatitude("-90")).isTrue();
        assertThat(LocationUtils.isValidLatitude("0")).isTrue();
        assertThat(LocationUtils.isValidLatitude("-91")).isFalse();
        assertThat(LocationUtils.isValidLatitude("91")).isFalse();

        assertThat(LocationUtils.isValidLatitude("regular string")).isFalse();
        assertThat(LocationUtils.isValidLatitude("")).isFalse();
    }

    @Test
    public void isValidLongitude_doubleInput() {
        assertThat(LocationUtils.isValidLongitude(180)).isTrue();
        assertThat(LocationUtils.isValidLongitude(-180)).isTrue();
        assertThat(LocationUtils.isValidLongitude(0)).isTrue();
        assertThat(LocationUtils.isValidLongitude(-181)).isFalse();
        assertThat(LocationUtils.isValidLongitude(181)).isFalse();
    }

    @Test
    public void isValidLongitude_stringInput() {
        assertThat(LocationUtils.isValidLongitude("180")).isTrue();
        assertThat(LocationUtils.isValidLongitude("-180")).isTrue();
        assertThat(LocationUtils.isValidLongitude("0")).isTrue();
        assertThat(LocationUtils.isValidLongitude("-181")).isFalse();
        assertThat(LocationUtils.isValidLongitude("181")).isFalse();

        assertThat(LocationUtils.isValidLongitude("regular string")).isFalse();
        assertThat(LocationUtils.isValidLongitude("")).isFalse();
    }
}
