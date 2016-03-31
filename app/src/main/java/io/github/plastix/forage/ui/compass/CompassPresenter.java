package io.github.plastix.forage.ui.compass;

import android.location.Location;
import android.util.Pair;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.sensor.AzimuthInteractor;
import io.github.plastix.forage.ui.Presenter;
import io.github.plastix.forage.util.AngleUtils;
import io.github.plastix.forage.util.LocationUtils;
import io.github.plastix.forage.util.RxUtils;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.Subscriptions;

public class CompassPresenter extends Presenter<CompassView> {

    private static long LOCATION_UPDATE_INTERVAL = 5000;
    private static long COMPASS_UPDATE_INTERVAL = 250;

    private AzimuthInteractor azimuthInteractor;
    private LocationInteractor locationInteractor;

    private Subscription subscription;
    private Location target;

    @Inject
    public CompassPresenter(AzimuthInteractor azimuthInteractor, LocationInteractor locationInteractor) {
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

    @Override
    public void onStop() {
        RxUtils.safeUnsubscribe(subscription);
    }

    @Override
    public void onResume() {
        updateCompass();
    }

    public void updateCompass() {
        this.subscription = Observable.combineLatest(
                azimuthInteractor.getAzimuthObservable(),
                locationInteractor.getLocationObservable(LOCATION_UPDATE_INTERVAL),
                new Func2<Float, Location, Pair<Float, Location>>() {
                    @Override
                    public Pair<Float, Location> call(Float azimuth, Location location) {
                        return new Pair<>(azimuth, location);
                    }
                }
        )
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
                .sample(COMPASS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
                .compose(RxUtils.<Pair<Float, Location>>subscribeOnComputationThreadTransformer())
                .compose(RxUtils.<Pair<Float, Location>>observeOnUIThreadTransformer())
                .subscribe(new Action1<Pair<Float, Location>>() {
                    @Override
                    public void call(Pair<Float, Location> pair) {
                        view.rotateCompass(pair.first);
                        view.updateDistance(pair.second.distanceTo(target));
                        view.updateAccuracy(pair.second.getAccuracy());
                    }
                });
    }
}
