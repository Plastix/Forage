package io.github.plastix.forage.data.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Gson Type Adapter for dealing with OkApi's awful JSON response format. For some reason
 * OkApi returns JSON Object with each each geocache as a key instead of a JSON list.
 * <p>
 * This adapter converts the OkApi JSON object format to a nice list.
 *
 * @param <T> Object type of list.
 */
public class ListTypeAdapter<T> extends TypeAdapter<List<T>> {

    private TypeAdapter<T> delegateAdapter;

    @Inject
    public ListTypeAdapter(TypeAdapter<T> delegateAdapter) {
        this.delegateAdapter = delegateAdapter;
    }

    @Override
    public void write(JsonWriter out, List<T> value) throws IOException {
        // Do nothing
    }

    @Override
    public List<T> read(JsonReader in) throws IOException {
        List<T> list = new ArrayList<>();

        in.beginObject();
        while (in.peek() != JsonToken.END_OBJECT) {
            // Ignore the JSON Key
            in.nextName();

            // Parse the object using the delegate's type adapter
            list.add(delegateAdapter.read(in));
        }

        return list;
    }
}
