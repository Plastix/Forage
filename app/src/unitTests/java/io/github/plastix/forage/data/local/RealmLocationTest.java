package io.github.plastix.forage.data.local;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.plastix.forage.data.local.model.RealmLocation;
import io.github.plastix.forage.util.LocationUtils;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LocationUtils.class)
public class RealmLocationTest {

    @Before
    public void beforeEachTest() {
        PowerMockito.mockStatic(LocationUtils.class);
    }

    @Test
    public void toLocation_shouldCallLocationUtils() {
        double lat = 40.7127;
        double lon = 74.0059;

        RealmLocation realmLocation = new RealmLocation();
        realmLocation.latitude = lat;
        realmLocation.longitude = lon;

        realmLocation.toLocation();

        PowerMockito.verifyStatic();
        LocationUtils.buildLocation(lat, lon);
    }

    @Test
    public void toLocation_shouldReturnFromLocationUtils() {
        double lat = 40.7127;
        double lon = 74.0059;

        RealmLocation realmLocation = new RealmLocation();
        realmLocation.latitude = lat;
        realmLocation.longitude = lon;

        Location location = mock(Location.class);
        when(LocationUtils.buildLocation(lat, lon)).thenReturn(location);

        Location result = realmLocation.toLocation();

        assertThat(result).isSameAs(location);
    }
}
