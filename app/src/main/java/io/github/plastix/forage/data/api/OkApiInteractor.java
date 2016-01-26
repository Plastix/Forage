package io.github.plastix.forage.data.api;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.plastix.forage.BuildConfig;
import io.github.plastix.forage.util.JsonUtils;
import io.github.plastix.forage.util.UnitUtils;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A Reactive wrapper around {@link OkApiService}.
 */
@Singleton
public class OkApiInteractor {

    private OkApiService apiService;

    @Inject
    public OkApiInteractor(OkApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Gets a JSON array of Geocaches near the specified Location from {@link OkApiService}.
     *
     * @param location Center location.
     * @return A rx.Single JsonArray.
     */
    public Single<JSONArray> getNearbyCaches(Location location, Double radius) {
        return apiService.searchAndRetrieve(
                OkApiService.ENDPOINT_NEAREST,
                String.format("{\"center\":\"%s|%s\", \"radius\":\"%s\"}", location.getLatitude(), location.getLongitude(), UnitUtils.milesToKilometer(radius)),
                OkApiService.ENDPOINT_GEOCACHES,
                "{\"fields\":\"code|name|location|type|status|terrain|difficulty|size2|description\"}",
                false,
                BuildConfig.OKAPI_US_CONSUMER_KEY
        ).map(new Func1<JSONObject, JSONArray>() {
            @Override
            public JSONArray call(JSONObject jsonObject) {
                return JsonUtils.jsonObjectToArray(jsonObject);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
