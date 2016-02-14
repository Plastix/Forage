package io.github.plastix.forage.ui.compass;

import android.location.Location;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.LifecycleCallbacks;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subscriptions.Subscriptions;

public class CompassPresenter implements LifecycleCallbacks {

    private static long LOCATION_UPDATE_INTERVAL = 500;

    private CompassView view;
    private AzimuthInteractor azimuthInteractor;
    private LocationInteractor locationInteractor;

    private Subscription subscription;
    private Location target;

    @Inject
    public CompassPresenter(CompassView view, AzimuthInteractor azimuthInteractor, LocationInteractor locationInteractor) {
        this.view = view;
        this.azimuthInteractor = azimuthInteractor;
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
                azimuthInteractor.getAzimuthObservable(),
                locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL)
                        .doOnNext(new Action1<Location>() {
                            @Override
                            public void call(Location location) {
                                view.updateDistance(target.distanceTo(location));
                            }
                        }),
                new Func2<Float, Location, Float>() {
                    @Override
                    public Float call(Float azimuth, Location location) {
                        azimuth += (float) LocationUtils.getMagneticDeclination(location);
                        float bearing = location.bearingTo(target);
                        return AngleUtils.normalize(azimuth - bearing);
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
