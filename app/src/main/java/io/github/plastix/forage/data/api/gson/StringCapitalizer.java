package io.github.plastix.forage.data.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import javax.inject.Inject;

import io.github.plastix.forage.util.StringUtils;

/**
 * Gson adapter for automatically capitalizing a String input.
 */
public class StringCapitalizer extends TypeAdapter<String> {

    /**
     * Single @Inject annotated constructor so Dagger knows how to instantiate the adapter.
     */
    @Inject
    public StringCapitalizer() {
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        // Nothing
    }

    @Override
    public String read(JsonReader in) throws IOException {
        String raw = in.nextString();
        return StringUtils.capitalize(raw);
    }
}
