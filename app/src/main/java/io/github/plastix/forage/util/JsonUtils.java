package io.github.plastix.forage.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static JSONArray jsonObjectToArray(JSONObject object) {
        try {
            return object.toJSONArray(object.names());
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}
