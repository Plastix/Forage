package io.github.plastix.forage.data.api.gson;

import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.plastix.forage.util.StringUtils;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StringUtils.class)
public class StringCaptializerTest {

    private StringCapitalizerAdapter stringCapitalizerAdapter;
    private JsonReader jsonReader;

    @Before
    public void beforeEachTest() {
        stringCapitalizerAdapter = new StringCapitalizerAdapter();
        jsonReader = mock(JsonReader.class);
        PowerMockito.mockStatic(StringUtils.class);
    }

    @Test
    public void read_shouldCallStringUtils() throws Exception {
        String input = "regular string";
        when(jsonReader.nextString()).thenReturn(input);

        stringCapitalizerAdapter.read(jsonReader);

        PowerMockito.verifyStatic();
        StringUtils.capitalize(input);
    }

    @Test
    public void read_returnsConvertedString() throws Exception {
        String input = "regular string";
        when(jsonReader.nextString()).thenReturn(input);

        String newString = "Regular string";
        when(StringUtils.capitalize(input)).thenReturn(newString);

        String result = stringCapitalizerAdapter.read(jsonReader);
        assertThat(result).isEqualTo(newString);
    }
}
