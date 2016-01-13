package io.github.plastix.forage.data.api;

import android.location.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.plastix.forage.BuildConfig;
import io.github.plastix.forage.util.JsonUtils;
import rx.Single;
import rx.functions.Func1;

/**
 * A light wrapper around {@link OkApiService}.
 */
@Singleton
public class OkApi {

    private OkApiService apiService;

    @Inject
    public OkApi(OkApiService apiService) {
        this.apiService = apiService;
    }

    public Single<JsonArray> getNearbyCaches(Location location) {
        return apiService.getNearbyCaches(
                OkApiService.ENDPOINT_NEAREST,
                String.format("{\"center\":\"%s|%s\"}", location.getLatitude(), location.getLongitude()),
                OkApiService.ENDPOINT_GEOCACHES,
                "{\"fields\":\"code|name|location|type|status|terrain|difficulty|size2|description\"}",
                false,
                BuildConfig.OKAPI_US_CONSUMER_KEY
        ).map(new Func1<JsonObject, JsonArray>() {
            @Override
            public JsonArray call(JsonObject jsonObject) {
                return JsonUtils.jsonObjectToArray(jsonObject);
            }
        }); // TODO Replace this map with a Retrofit converter?
    }
}
