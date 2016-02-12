package io.github.plastix.forage.ui.compass;

import android.hardware.GeomagneticField;
import android.location.Location;

import javax.inject.Inject;

import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.LifecycleCallbacks;
import rx.Notification;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subscriptions.Subscriptions;

public class CompassPresenter implements LifecycleCallbacks {

    private static long LOCATION_UPDATE_INTERVAL = 1000;

    private CompassView view;
    private AzimuthInteractor asimuthInteractor;
    private LocationInteractor locationInteractor;

    private Subscription subscription;
    private Location target;

    @Inject
    public CompassPresenter(CompassView view, AzimuthInteractor azimuthInteractor, LocationInteractor locationInteractor) {
        this.view = view;
        this.asimuthInteractor = azimuthInteractor;
        this.locationInteractor = locationInteractor;
        this.subscription = Subscriptions.empty();
    }

    public void setTargetLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Target location cannot be null!");
        }
        this.target = location;
    }

    public void updateCompass() {
        this.subscription = Observable.combineLatest(
                asimuthInteractor.getAzimuthObservable(),
                locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL),
                new Func2<Float, Location, Float>() {
                    @Override
                    public Float call(Float azimuth, Location location) {
                        // TODO Factor this out
                        GeomagneticField geoField = new GeomagneticField(
                                (float) location.getLatitude(),
                                (float) location.getLongitude(),
                                (float) location.getAltitude(),
                                System.currentTimeMillis()
                        );
                        azimuth += geoField.getDeclination();
                        float bearing = location.bearingTo(target);
                        return azimuth - bearing;
                    }
                }
        ).subscribe(new Action1<Float>() {
            @Override
            public void call(Float degrees) {
                view.rotateCompass(degrees);
            }
        });
    }

    private void unSubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        unSubscribe();
    }

    @Override
    public void onResume() {
        unSubscribe();
        updateCompass();
    }
}
