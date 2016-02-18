package io.github.plastix.forage.data.location;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Observable subscriber wrapper around GoogleAPI connection callbacks.
 */
public class LocationOnSubscribe implements Observable.OnSubscribe<Location>, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient googleApiClient;
    private Observer<? super Location> observable;
    private LocationListener listener;
    private LocationRequest locationRequest;

    @Inject
    public LocationOnSubscribe(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    @Override
    public void call(Subscriber<? super Location> subscriber) {
        this.observable = subscriber;
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.registerConnectionFailedListener(this);

        if(!googleApiClient.isConnected() && !googleApiClient.isConnecting()){
            googleApiClient.connect();
        }

        subscriber.add(buildUnsubscriber());
    }

    private Subscription buildUnsubscriber() {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);
                googleApiClient.unregisterConnectionCallbacks(LocationOnSubscribe.this);
                googleApiClient.unregisterConnectionFailedListener(LocationOnSubscribe.this);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        listener = buildListener();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);
    }

    private LocationListener buildListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                observable.onNext(location);
            }
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        observable.onError(new Throwable("Connection lost to Google Play Services"));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        observable.onError(new Throwable("Failed to connect to Google Play Services!"));

    }
}