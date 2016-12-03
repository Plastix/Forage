package io.github.plastix.forage.data.location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

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
                locationAsyncEmitter.onError(new LocationUnavailableException("Failed to connect to Google Play Services!"));

        ResultCallback<LocationSettingsResult> pendingResultCallback = locationSettingsResult -> {
            Status status = locationSettingsResult.getStatus();
            if (status.getStatusCode() == LocationSettingsStatusCodes.SUCCESS) {
                locationAsyncEmitter.onCompleted();
            } else {
                locationAsyncEmitter.onError(new LocationUnavailableException("Location services not enabled!"));
            }
        };

        GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                try {
                    LocationRequest request = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(request)
                            .setAlwaysShow(true);

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

                    result.setResultCallback(pendingResultCallback);

                } catch (SecurityException e) {
                    locationAsyncEmitter.onError(new LocationUnavailableException("Location permission not available?"));
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                locationAsyncEmitter.onError(new LocationUnavailableException("Connection lost to Google Play Services"));

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
