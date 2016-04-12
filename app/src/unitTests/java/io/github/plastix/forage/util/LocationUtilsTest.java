package io.github.plastix.forage.util;

import android.location.Location;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(Enclosed.class)
public class LocationUtilsTest {

    @RunWith(ForageRoboelectricUnitTestRunner.class)
    public static class WithRoboelectric {

        @Test
        public void buildLocation_returnsCorrectData() {
            double lat = 43;
            double lon = 72;

            Location location = LocationUtils.buildLocation(lat, lon);

            assertThat(location.getLatitude()).isWithin(0).of(lat);
            assertThat(location.getLongitude()).isWithin(0).of(lon);
            assertThat(location.getProvider()).isEmpty();
        }

        @Test
        public void getMagnetDeclination_returnsCorrectDeclination() {
            Location location = new Location("");
            location.setLatitude(0);
            location.setLongitude(0);

            assertThat(LocationUtils.getMagneticDeclination(location)).isWithin(0.05).of(-5.39);

            location.setLatitude(40.7128);
            location.setLongitude(74.0059);
            assertThat(LocationUtils.getMagneticDeclination(location)).isWithin(0.05).of(4.4);

        }

    }

    public static class WithoutRoboelectric {

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
}
