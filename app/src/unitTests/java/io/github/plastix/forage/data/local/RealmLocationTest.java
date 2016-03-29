package io.github.plastix.forage.data.local;

import android.location.Location;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricUnitTestRunner;
import io.github.plastix.forage.data.local.model.RealmLocation;

import static com.google.common.truth.Truth.assertThat;

@RunWith(ForageRoboelectricUnitTestRunner.class)
public class RealmLocationTest {

    @Test
    public void toLocation_shouldReturnCorrectLocation() {
        double lat = 40.7127;
        double lon = 74.0059;

        RealmLocation realmLocation = new RealmLocation();
        realmLocation.latitude = lat;
        realmLocation.longitude = lon;

        Location location = realmLocation.toLocation();

        assertThat(location.getLatitude()).isWithin(0.0).of(lat);
        assertThat(location.getLongitude()).isWithin(0.0).of(lon);
    }
}
