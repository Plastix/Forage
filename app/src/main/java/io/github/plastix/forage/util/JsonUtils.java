package io.github.plastix.forage.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonUtils {

    public static JsonArray jsonObjectToArray(JsonObject object) {
        JsonArray array = new JsonArray();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            array.add(object.getAsJsonObject(entry.getKey()));
        }
        return array;
    }
}
