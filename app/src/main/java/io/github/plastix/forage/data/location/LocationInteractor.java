package io.github.plastix.forage.data.location;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.github.plastix.forage.ui.LifecycleCallbacks;
import rx.Completable;
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
public class LocationInteractor {

    private LocationObservableFactory observableFactory;
    private LocationCompletableFactory completableFactory;

    @Inject
    public LocationInteractor(LocationObservableFactory observableFactory, LocationCompletableFactory completableFactory) {
        this.observableFactory = observableFactory;
        this.completableFactory = completableFactory;
    }

    /**
     * Gets the updated location using Google Play Location Services.
     * The caller must have location permissions before calling this method!
     *
     * @return An rx.Observable that emits one Location object.
     */
    public Observable<Location> getUpdatedLocation() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setExpirationDuration(2500);

        return observableFactory.buildObservable(request).take(1);
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


        return observableFactory.buildObservable(request);
    }

    /**
     * Returns whether location is available on the device using Google Play Location Services using an
     * RxJava Completable.
     *
     * @return Completable calls onComplete() when location is available, and onError() when not.
     */
    public Completable isLocationAvailable() {
        return completableFactory.buldLocationCompletable();
    }

}
