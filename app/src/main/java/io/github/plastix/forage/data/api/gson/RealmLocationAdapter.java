package io.github.plastix.forage.data.api.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.github.plastix.forage.data.local.model.RealmLocation;

public class RealmLocationAdapter extends TypeAdapter<RealmLocation> {

    @Override
    public void write(JsonWriter out, RealmLocation value) throws IOException {

    }

    @Override
    public RealmLocation read(JsonReader in) throws IOException {
        String raw = in.nextString();
        final String[] parts = raw.split("\\|");

        RealmLocation location = new RealmLocation();
        location.latitude = Double.parseDouble(parts[0]);
        location.longitude = Double.parseDouble(parts[1]);
        return location;
    }
}
