package io.github.plastix.forage.ui.map;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.realm.OrderedRealmCollection;
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
                        .toObservable()
                        .compose(this.<OrderedRealmCollection<Cache>>deliverFirst())
                        .toSingle()
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
        addSubscription(locationInteractor.getUpdatedLocation()
                .toObservable()
                .compose(deliverFirst())
                .toSingle()
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
