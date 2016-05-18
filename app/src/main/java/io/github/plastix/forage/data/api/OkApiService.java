package io.github.plastix.forage.data.api;

import java.util.List;

import io.github.plastix.forage.data.api.response.SubmitLogResponse;
import io.github.plastix.forage.data.local.model.Cache;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Single;

/**
 * Retrofit service definition of the Open Caching API.
 * http://www.opencaching.us/okapi/introduction.html
 */
public interface OkApiService {

    @GET("/okapi/services/caches/shortcuts/search_and_retrieve")
    Single<List<Cache>> searchAndRetrieve(@Query("search_method") String searchMethod,
                                          @Query("search_params") String searchParams,
                                          @Query("retr_method") String retrMethod,
                                          @Query("retr_params") String retrParams,
                                          @Query("wrap") boolean wrap,
                                          @Query("consumer_key") String consumerKey
    );

    @Headers("OAuth: ENABLED")
    @GET("/okapi/services/logs/submit")
    Single<SubmitLogResponse> submitLog(@Query("cache_code") String cacheCode,
                                        @Query("logtype") String logType,
                                        @Query("comment") String comment
    );


}
