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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public void startCompass(){
        if (!enabled) {
            rotateCompass();
            enabled = !enabled;
        }
    }

    private void rotateCompass() {
        addSubscription(
                Observable.combineLatest(
                        azimuthInteractor.getAzimuthObservable(),
                        locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL),
                        new Func2<Float, Location, Pair<Float, Location>>() {
                            @Override
                            public Pair<Float, Location> call(Float azimuth, Location location) {
                                return new Pair<>(azimuth, location);
                            }
                        })
                        .compose(RxUtils.<Pair<Float, Location>>subscribeOnComputationThreadTransformer())
                        .map(new Func1<Pair<Float, Location>, Pair<Float, Location>>() {
                            @Override
                            public Pair<Float, Location> call(Pair<Float, Location> pair) {
                                float azimuth = pair.first;
                                Location location = pair.second;

                                azimuth += (float) LocationUtils.getMagneticDeclination(location);
                                float bearing = location.bearingTo(target);

                                return new Pair<>(AngleUtils.normalize(azimuth - bearing), location);

                            }
                        })
                        .compose(RxUtils.<Pair<Float, Location>>observeOnUIThreadTransformer())
                        .throttleFirst(COMPASS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                        .compose(this.<Pair<Float, Location>>deliverLatest())
                        .subscribe(new Action1<Pair<Float, Location>>() {
                            @Override
                            public void call(Pair<Float, Location> pair) {
                                if (isViewAttached()) {
                                    view.rotateCompass(pair.first);
                                    view.updateDistance(pair.second.distanceTo(target));
                                    view.updateAccuracy(pair.second.getAccuracy());
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO Show error dialog on view
                                throwable.printStackTrace();
                            }
                        }));
    }

    @Override
    public void onDestroyed() {
        azimuthInteractor = null;
        locationInteractor = null;
        target = null;
    }
}
