package io.github.plastix.forage.data.api;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface OkApiService {

    String ENDPOINT_GEOCACHES = "services/caches/geocaches";
    String ENDPOINT_NEAREST = "services/caches/search/nearest";

    @GET("/okapi/services/caches/shortcuts/search_and_retrieve")
    Single<JsonObject> getNearbyCaches(@Query("search_method") String search_method,
                                       @Query("search_params") String search_params,
                                       @Query("retr_method") String retr_method,
                                       @Query("retr_params") String retr_params,
                                       @Query("wrap") boolean wrap,
                                       @Query("consumer_key") String consumer_key);
}
