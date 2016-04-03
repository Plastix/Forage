package io.github.plastix.forage.data.api.gson;

import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.forage.data.local.model.RealmLocation;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RealmLocationAdapterTest {

    private RealmLocationAdapter realmLocationAdapter;
    private JsonReader jsonReader;

    @Before
    public void beforeEachTest() {
        realmLocationAdapter = new RealmLocationAdapter();
        jsonReader = mock(JsonReader.class);
    }

    @Test
    public void read_shouldReturnRealmLocation() throws Exception {
        String input = "0|0";
        when(jsonReader.nextString()).thenReturn(input);

        assertThat(realmLocationAdapter.read(jsonReader)).isInstanceOf(RealmLocation.class);
    }

    @Test
    public void read_shouldReturnCorrectData() throws Exception {
        double lat = 43;
        double lon = 72;
        String input = lat + "|" + lon;
        when(jsonReader.nextString()).thenReturn(input);

        RealmLocation location = realmLocationAdapter.read(jsonReader);
        assertThat(location.latitude).isWithin(0).of(lat);
        assertThat(location.longitude).isWithin(0).of(lon);
    }

    @Test
    public void read_shouldReturnNullOnEmptyString() throws Exception {
        when(jsonReader.nextString()).thenReturn("");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();
    }

    @Test
    public void read_shouldReturnNullOnNullString() throws Exception {
        when(jsonReader.nextString()).thenReturn(null);
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();
    }

    @Test
    public void read_shouldReturnNullInvalidString() throws Exception {
        when(jsonReader.nextString()).thenReturn(" ");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();

        when(jsonReader.nextString()).thenReturn("regular string");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();

        when(jsonReader.nextString()).thenReturn("hello|world");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();

        when(jsonReader.nextString()).thenReturn("1234");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();

        when(jsonReader.nextString()).thenReturn("42|world");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();

        when(jsonReader.nextString()).thenReturn("hello|42");
        assertThat(realmLocationAdapter.read(jsonReader)).isNull();
    }
}
