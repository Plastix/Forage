package io.github.plastix.forage.data.location;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.ForApplication;

@Module
public class LocationModule {

    @Provides
    @Singleton
    public GoogleApiClient provideGoogleApiClient(@ForApplication Context context) {
        return new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
    }
}
