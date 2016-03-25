package io.github.plastix.forage.data.api;


import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.data.api.gson.HtmlAdapter;
import io.github.plastix.forage.data.api.gson.ListTypeAdapterFactory;
import io.github.plastix.forage.data.api.gson.StringCapitalizer;
import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides dependencies for {@link OkApiService} and {@link OkApiInteractor}.
 */
@Module
public class OkApiModule {

    public static final String BASE_ENDPOINT = "http://www.opencaching.us";

    @NonNull
    @Provides
    @Singleton
    @Named("BASE_ENDPOINT")
    public String provideBaseURL() {
        return BASE_ENDPOINT;
    }

    @NonNull
    @Provides
    @Singleton
    public Retrofit provideRetrofit(@NonNull @Named("BASE_ENDPOINT") String baseUrl,
                                    @NonNull GsonConverterFactory gsonConverter,
                                    @NonNull RxJavaCallAdapterFactory rxAdapter) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverter)
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    @NonNull
    @Provides
    @Singleton
    public GsonConverterFactory provideGsonConverterFactory(@NonNull Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    /**
     * Custom Gson to make Retrofit Gson adapter work with Realm objects
     */
    @NonNull
    @Provides
    @Singleton
    public Gson provideGson(@NonNull ListTypeAdapterFactory jsonArrayTypeAdapterFactory,
                            @NonNull HtmlAdapter htmlAdapter,
                            @NonNull StringCapitalizer stringCapitalizer) {

        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .registerTypeAdapterFactory(jsonArrayTypeAdapterFactory)
                .registerTypeAdapter(String.class, htmlAdapter)
                .registerTypeAdapter(String.class, stringCapitalizer)
                .create();
    }

    @NonNull
    @Provides
    @Singleton
    public RxJavaCallAdapterFactory providesRxAdapter() {
        return RxJavaCallAdapterFactory.create();
    }

    @NonNull
    @Provides
    @Singleton
    public OkApiService provideOkApiService(@NonNull Retrofit retrofit) {
        return retrofit.create(OkApiService.class);
    }

}
