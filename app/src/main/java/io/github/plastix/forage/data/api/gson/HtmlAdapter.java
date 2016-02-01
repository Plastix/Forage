package io.github.plastix.forage.data.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import javax.inject.Inject;

import io.github.plastix.forage.util.StringUtils;

/**
 * Gson adapter for converting OkApi's HTML to raw text.
 */
public class HtmlAdapter extends TypeAdapter<String> {

    /**
     * Single @Inject annotated constructor so Dagger knows how to instantiate the adapter.
     */
    @Inject
    public HtmlAdapter() {
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        // Nothing
    }

    @Override
    public String read(JsonReader in) throws IOException {
        String raw = in.nextString();
        return StringUtils.stripHtml(raw);
    }


}
