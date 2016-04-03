package io.github.plastix.forage.data.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.regex.Pattern;

import io.github.plastix.forage.data.local.model.RealmLocation;

public class RealmLocationAdapter extends TypeAdapter<RealmLocation> {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

    @Override
    public void write(JsonWriter out, RealmLocation value) throws IOException {

    }

    @Override
    public RealmLocation read(JsonReader in) throws IOException {
        String raw = in.nextString();

        if (raw == null || raw.isEmpty()) {
            return null;
        }
        final String[] parts = SPLIT_PATTERN.split(raw, 2);

        if (parts.length < 2) {
            return null;
        }

        try {
            RealmLocation location = new RealmLocation();
            location.latitude = Double.parseDouble(parts[0]);
            location.longitude = Double.parseDouble(parts[1]);
            return location;
        } catch (NumberFormatException e) {
            return null;
        }

    }
}
