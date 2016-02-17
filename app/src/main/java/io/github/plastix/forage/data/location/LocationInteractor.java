package io.github.plastix.forage.data.location;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.plastix.forage.ui.LifecycleCallbacks;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Reactive wrapper around Google Play Location Services.
 */
@Singleton
public class LocationInteractor implements LifecycleCallbacks {

    private GoogleApiClient apiClient;

    @Inject
    public LocationInteractor(GoogleApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Gets the updated location using Google Play Location Services.
     * The caller must have location permissions before calling this method!
     *
     * @return A rx.Single Location.
     */
    public Observable<Location> getUpdatedLocation() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(2500);

        return Observable.create(new LocationOnSubscribe(request)).take(1);
    }

    /**
     * Returns an Observable that emits Location events at the specified interval. This uses the Google
     * Play Services Location API.
     * The caller must have location permissions before calling this method!
     *
     * @param intervalInMillis Time between location events in milliseconds.
     * @return Location observable.
     */
    public Observable<Location> getLocationObservable(long intervalInMillis) {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(intervalInMillis)
                .setInterval(intervalInMillis);

        return Observable.create(new LocationOnSubscribe(request));
    }

    /**
     * Returns whether location is available on the device using Google Play Location Services.
     *
     * @return True if location is available, else false.
     */
    public boolean isLocationAvailable() {
        return LocationServices.FusedLocationApi.getLocationAvailability(apiClient).isLocationAvailable();
    }

    /**
     * Lifecycle callback used to connect to Google Play Services.
     */
    @Override
    public void onStart() {
        connectToGoogleApi();
    }

    private void connectToGoogleApi() {
        if (!isGoogleApiConnected()) {
            apiClient.connect();
        }
    }

    private boolean isGoogleApiConnected() {
        return apiClient.isConnected() || apiClient.isConnecting();
    }

    /**
     * Lifecycle callback used to disconnect from Google Play Services.
     */
    @Override
    public void onStop() {
        if (isGoogleApiConnected()) {
            this.apiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        // Unused
    }

    @Override
    public void onPause() {

    }

    /**
     * Observable subscriber wrapper around GoogleAPI connection callbacks.
     */
    private class LocationOnSubscribe implements Observable.OnSubscribe<Location>, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

        private Observer<? super Location> observable;
        private LocationListener listener;
        private LocationRequest locationRequest;

        public LocationOnSubscribe(LocationRequest locationRequest) {
            this.locationRequest = locationRequest;
        }

        @Override
        public void call(Subscriber<? super Location> subscriber) {
            this.observable = subscriber;
            apiClient.registerConnectionCallbacks(this);
            apiClient.registerConnectionFailedListener(this);

            connectToGoogleApi();

            subscriber.add(buildUnsubscriber());
        }

        private Subscription buildUnsubscriber() {
            return Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, listener);
                }
            });
        }

        @Override
        public void onConnected(Bundle bundle) {
            listener = buildListener();
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, listener);
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


}
