package io.github.plastix.forage.data.location;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;

/**
 * Dagger module to provide a location dependencies.
 */
@Module
public class LocationModule {

    @NonNull
    @Provides
    @Singleton
    public GoogleApiClient provideGoogleApiClient(@NonNull @ForApplication Context context) {
        return new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
    }
}
