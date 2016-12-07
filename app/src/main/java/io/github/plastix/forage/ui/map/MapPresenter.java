package io.github.plastix.forage.ui.map;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.ui.base.RxPresenter;
import io.github.plastix.rxdelay.RxDelay;
import timber.log.Timber;

public class MapPresenter extends RxPresenter<MapActivityView> {

    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;

    @Inject
    public MapPresenter(DatabaseInteractor databaseInteractor,
                        LocationInteractor locationInteractor) {
        this.databaseInteractor = databaseInteractor;
        this.locationInteractor = locationInteractor;
    }

    public void setupMap() {
        addSubscription(
                databaseInteractor.getGeocaches()
                        .compose(RxDelay.delaySingle(getViewState()))
                        .subscribe(caches -> {
                            if (isViewAttached()) {
                                view.addMapMarkers(caches);
                            }
                        }, throwable -> {
                            // TODO Dialog
                        })
        );

    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void centerMapOnLocation() {
        addSubscription(
                locationInteractor.isLocationAvailable()
                        .andThen(locationInteractor.getUpdatedLocation())
                        .compose(RxDelay.delaySingle(getViewState()))
                        .subscribe(location -> {
                                    if (isViewAttached()) {
                                        view.animateMapCamera(location);
                                    }
                                }, throwable -> Timber.e(throwable, "Error fetching location!")

                        )
        );
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
        databaseInteractor = null;
        locationInteractor = null;
    }
}
