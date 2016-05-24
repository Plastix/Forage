package io.github.plastix.forage.data.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.observers.SafeSubscriber;
import rx.subscriptions.Subscriptions;

/**
 * Observable subscriber wrapper around GoogleAPI connection callbacks.
 */
public class LocationOnSubscribe implements Observable.OnSubscribe<Location>, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient googleApiClient;
    private Subscriber<? super Location> subscriber;
    private LocationListener listener;
    private LocationRequest locationRequest;

    @Inject
    public LocationOnSubscribe(@NonNull GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    @Override
    public void call(Subscriber<? super Location> subscriber) {
        this.subscriber = subscriber;

        // Wrap the Subscriber to obey the Observable lifecycle
        if (!(this.subscriber instanceof SafeSubscriber)) {
            this.subscriber = new SafeSubscriber<>(subscriber);
        }

        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);
        googleApiClient.connect();
        subscriber.add(buildUnsubscriber());
    }

    private Subscription buildUnsubscriber() {
        return Subscriptions.create(() -> {
            this.locationRequest = null;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
            googleApiClient.unregisterConnectionCallbacks(LocationOnSubscribe.this);
            googleApiClient.unregisterConnectionFailedListener(LocationOnSubscribe.this);
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        listener = buildListener();
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);
        } catch (SecurityException e) {
            subscriber.onError(new Throwable("Location permission not available!"));
        }
    }

    private LocationListener buildListener() {
        return location -> subscriber.onNext(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        subscriber.onError(new Throwable("Connection lost to Google Play Services"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        subscriber.onError(new Throwable("Failed to connect to Google Play Services!"));
    }
}