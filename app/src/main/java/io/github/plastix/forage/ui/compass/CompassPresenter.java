package io.github.plastix.forage.ui.compass;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;
import io.github.plastix.forage.util.RxUtils;
import rx.Observable;

public class CompassPresenter extends RxPresenter<CompassView> {

    private static long LOCATION_UPDATE_INTERVAL = 5000;
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

    public void setTargetLocation(@NonNull Location location) {
        this.target = location;
    }

    public void startCompass() {
        locationInteractor.isLocationAvailable().subscribe(() -> {
            if (!enabled) {
                rotateCompass();
                enabled = !enabled;
            }

        }, throwable -> {
            if (isViewAttached()) {
                view.showLocationUnavailableDialog();
            }
        });
    }

    private void rotateCompass() {
        addSubscription(
                Observable.combineLatest(
                        azimuthInteractor.getAzimuthObservable(),
                        locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL),
                        Pair::new)
                        .compose(RxUtils.<Pair<Float, Location>>subscribeOnComputationThreadTransformer())
                        .map(pair -> {
                            float azimuth = pair.first;
                            Location location = pair.second;

                            azimuth += (float) LocationUtils.getMagneticDeclination(location);
                            float bearing = location.bearingTo(target);

                            return new Pair<>(AngleUtils.normalize(azimuth - bearing), location);

                        })
                        .compose(RxUtils.<Pair<Float, Location>>observeOnUIThreadTransformer())
                        .throttleFirst(COMPASS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                        .compose(this.<Pair<Float, Location>>deliverLatest())
                        .subscribe(pair -> {
                            if (isViewAttached()) {
                                view.rotateCompass(pair.first);
                                view.setDistance(pair.second.distanceTo(target));
                                view.setAccuracy(pair.second.getAccuracy());
                            }
                        }, Throwable::printStackTrace)
        );
    }

    @Override
    public void onDestroyed() {
        azimuthInteractor = null;
        locationInteractor = null;
        target = null;
    }
}
