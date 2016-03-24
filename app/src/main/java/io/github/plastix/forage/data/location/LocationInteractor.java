package io.github.plastix.forage.data.location;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;

/**
 * Reactive wrapper around Google Play Location Services.
 */
public class LocationInteractor {

    private static final int LOCATION_FASTEST_INTERVAL_MILLIS = 250;

    private LocationObservableFactory observableFactory;
    private LocationCompletableFactory completableFactory;

    @Inject
    public LocationInteractor(@NonNull LocationObservableFactory observableFactory,
                              @NonNull LocationCompletableFactory completableFactory) {
        this.observableFactory = observableFactory;
        this.completableFactory = completableFactory;
    }

    /**
     * Gets the updated location using Google Play Location Services.
     * The caller must have location permissions before calling this method!
     *
     * @return An rx.Observable that emits one Location object.
     */
    @NonNull
    public Observable<Location> getUpdatedLocation() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);

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
    @NonNull
    public Observable<Location> getLocationObservable(long intervalInMillis) {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(LOCATION_FASTEST_INTERVAL_MILLIS)
                .setInterval(intervalInMillis);


        return observableFactory.buildObservable(request);
    }

    /**
     * Returns whether location is available on the device using Google Play Location Services using an
     * RxJava Completable.
     *
     * @return Completable calls onComplete() when location is available, and onError() when not.
     */
    @NonNull
    public Completable isLocationAvailable() {
        return completableFactory.buldLocationCompletable();
    }

}
