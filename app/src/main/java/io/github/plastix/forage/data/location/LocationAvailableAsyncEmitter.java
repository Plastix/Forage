package io.github.plastix.forage.data.location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.AsyncEmitter;
import rx.functions.Action1;

public class LocationAvailableAsyncEmitter implements Action1<AsyncEmitter<Void>> {

    private final GoogleApiClient googleApiClient;

    @Inject
    public LocationAvailableAsyncEmitter(@NonNull GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Override
    public void call(AsyncEmitter<Void> locationAsyncEmitter) {

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = connectionResult ->
                locationAsyncEmitter.onError(new Throwable("Failed to connect to Google Play Services!"));

        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                try {
                    boolean locationAvailable = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable();

                    if (locationAvailable) {
                        locationAsyncEmitter.onCompleted();
                    } else {
                        locationAsyncEmitter.onError(new Throwable("Location not available!"));
                    }

                } catch (SecurityException e) {
                    locationAsyncEmitter.onError(new Throwable("Location permission not available?"));
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                locationAsyncEmitter.onError(new Throwable("Connection lost to Google Play Services"));

            }
        };

        googleApiClient.registerConnectionCallbacks(connectionCallbacks);
        googleApiClient.registerConnectionFailedListener(onConnectionFailedListener);
        googleApiClient.connect();

        locationAsyncEmitter.setCancellation(() -> {
            googleApiClient.unregisterConnectionCallbacks(connectionCallbacks);
            googleApiClient.unregisterConnectionFailedListener(onConnectionFailedListener);
        });
    }
}
