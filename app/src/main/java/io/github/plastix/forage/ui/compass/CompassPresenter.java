package io.github.plastix.forage.ui.compass;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.base.RxPresenter;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;
import io.github.plastix.forage.util.RxUtils;
import io.github.plastix.rxdelay.RxDelay;
import rx.Observable;

public class CompassPresenter extends RxPresenter<CompassView> {

    private static long LOCATION_UPDATE_INTERVAL = 1000;
    private static long COMPASS_UPDATE_INTERVAL = 250;

    private AzimuthInteractor azimuthInteractor;
    private LocationInteractor locationInteractor;
    private Location target;

    private boolean enabled;

    @Inject
    public CompassPresenter(AzimuthInteractor azimuthInteractor, LocationInteractor locationInteractor) {
        this.azimuthInteractor = azimuthInteractor;
        this.locationInteractor = locationInteractor;
        this.enabled = false;
    }

    public void startCompass(@NonNull Location location) {
        target = location;

        if (enabled) {
            if (isViewAttached()) {
                view.showCompass();
            }
        } else {
            locationInteractor.isLocationAvailable().subscribe(() -> {
                rotateCompass();
                enabled = true;
            }, throwable -> {
                if (isViewAttached()) {
                    view.showLocationUnavailableDialog();
                }
            });
        }
    }

    private void rotateCompass() {
        addSubscription(
                Observable.combineLatest(
                        // If we get an error from the Azimuth observable it means we don't have a
                        // compass sensor. Thus, send 0 downstream and only use the location
                        azimuthInteractor.getAzimuthObservable().onErrorReturn(throwable -> 0f),
                        locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL),
                        Pair::new)
                        .compose(RxUtils.subscribeOnComputationThreadTransformer())
                        .map(this::calculateAngle)
                        .compose(RxUtils.observeOnUIThreadTransformer())
                        .throttleFirst(COMPASS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                        .compose(RxDelay.delayLatest(getViewState()))
                        .compose(RxUtils.doOnFirst(floatLocationPair -> {
                            if (isViewAttached()) {
                                view.showCompass();
                            }
                        }))
                        .subscribe(pair -> {
                            if (isViewAttached()) {
                                view.rotateCompass(pair.first);
                                view.setDistance(pair.second.distanceTo(target));
                                view.setAccuracy(pair.second.getAccuracy());
                            }
                        }, Throwable::printStackTrace)
        );
    }

    private Pair<Float, Location> calculateAngle(Pair<Float, Location> pair) {
        float azimuth = pair.first;
        Location location = pair.second;

        azimuth += (float) LocationUtils.getMagneticDeclination(location);
        float bearing = location.bearingTo(target);

        return Pair.create(AngleUtils.normalize(azimuth - bearing), location);
    }

    @Override
    public void onDestroyed() {
        azimuthInteractor = null;
        locationInteractor = null;
        target = null;
    }
}
