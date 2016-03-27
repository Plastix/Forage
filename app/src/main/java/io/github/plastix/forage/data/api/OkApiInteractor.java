package io.github.plastix.forage.data.api;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.github.plastix.forage.BuildConfig;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.util.RxUtils;
import io.github.plastix.forage.util.StringUtils;
import io.github.plastix.forage.util.UnitUtils;
import rx.Observable;

/**
 * A Reactive wrapper around {@link OkApiService}.
 */
public class OkApiInteractor {

    private Lazy<OkApiService> apiService;
    private String[] geocacheFields = {"code", "name", "location", "type", "status", "terrain",
            "difficulty", "size2", "description"};

    @Inject
    public OkApiInteractor(@NonNull Lazy<OkApiService> apiService) {
        this.apiService = apiService;
    }

    /**
     * Gets a JSON array of Geocaches near the specified Location from {@link OkApiService}.
     *
     * @param lat    Latitude of specified location.
     * @param lon    Longitude of specified location.
     * @param radius Boundary radius.
     * @return A rx.Single JsonArray.
     */
    public Observable<List<Cache>> getNearbyCaches(double lat, double lon, double radius) {

        JsonObject searchParams = new JsonObject();
        searchParams.addProperty("center", String.format("%s|%s", lat, lon));
        searchParams.addProperty("radius", UnitUtils.milesToKilometer(radius));

        JsonObject returnParams = new JsonObject();
        returnParams.addProperty("fields", StringUtils.join("|", geocacheFields));

        return apiService.get().searchAndRetrieve(
                OkApiService.ENDPOINT_NEAREST,
                searchParams.toString(),
                OkApiService.ENDPOINT_GEOCACHES,
                returnParams.toString(),
                false,
                BuildConfig.OKAPI_US_CONSUMER_KEY
        ).compose(RxUtils.<List<Cache>>applySchedulers());
    }
}
