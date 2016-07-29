package io.github.plastix.forage.data.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.AsyncEmitter;
import rx.functions.Action1;

public class LocationAsyncEmitter implements Action1<AsyncEmitter<Location>> {

    private final GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Inject
    public LocationAsyncEmitter(@NonNull GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        this.locationRequest = null;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    @Override
    public void call(AsyncEmitter<Location> locationAsyncEmitter) {

        LocationListener locationListener = locationAsyncEmitter::onNext;

        GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = connectionResult ->
                locationAsyncEmitter.onError(new Throwable("Failed to connect to Google Play Services!"));

        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
                } catch (SecurityException e) {
                    locationAsyncEmitter.onError(new Throwable("Location permission not available!"));
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
            LocationAsyncEmitter.this.locationRequest = null;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            googleApiClient.unregisterConnectionCallbacks(connectionCallbacks);
            googleApiClient.unregisterConnectionFailedListener(onConnectionFailedListener);
        });
    }
}
