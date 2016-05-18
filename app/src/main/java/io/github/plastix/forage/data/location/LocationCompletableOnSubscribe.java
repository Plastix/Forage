package io.github.plastix.forage.data.location;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.Completable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class LocationCompletableOnSubscribe implements Completable.CompletableOnSubscribe, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Completable.CompletableSubscriber completableSubscriber;
    private GoogleApiClient googleApiClient;

    @Inject
    public LocationCompletableOnSubscribe(@NonNull GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    @Override
    public void call(Completable.CompletableSubscriber completableSubscriber) {
        this.completableSubscriber = completableSubscriber;
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();
        completableSubscriber.onSubscribe(buildUnsubscriber());
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            boolean locationAvailable = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient).isLocationAvailable();

            if (locationAvailable) {
                completableSubscriber.onCompleted();
            } else {
                completableSubscriber.onError(new Throwable("Location not available!"));
            }

        } catch (SecurityException e) {
            completableSubscriber.onError(new Throwable("Location permission not available?"));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        completableSubscriber.onError(new Throwable("Connection lost to Google Play Services"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        completableSubscriber.onError(new Throwable("Failed to connect to Google Play Services!"));

    }

    private Subscription buildUnsubscriber() {
        return Subscriptions.create(() -> {
            googleApiClient.unregisterConnectionCallbacks(LocationCompletableOnSubscribe.this);
            googleApiClient.unregisterConnectionFailedListener(LocationCompletableOnSubscribe.this);
        });
    }
}
