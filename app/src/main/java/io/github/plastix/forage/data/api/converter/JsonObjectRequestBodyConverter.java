package io.github.plastix.forage.data.api.converter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class JsonObjectRequestBodyConverter implements Converter<ResponseBody, JSONObject> {

    @Override
    public JSONObject convert(ResponseBody value) throws IOException {
        try {
            return new JSONObject(value.string());
        } catch (JSONException e) {

            throw new IOException(e.getCause());
        }
    }
}
