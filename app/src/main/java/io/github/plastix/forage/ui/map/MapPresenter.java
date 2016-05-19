package io.github.plastix.forage.ui.map;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.realm.OrderedRealmCollection;

public class MapPresenter extends RxPresenter<MapFragView> {

    private DatabaseInteractor databaseInteractor;

    @Inject
    public MapPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocaches() {
        addSubscription(
                databaseInteractor.getGeocaches()
                        .toObservable()
                        .compose(this.<OrderedRealmCollection<Cache>>deliverFirst())
                        .toSingle()
                        .subscribe(caches -> {
                            if (isViewAttached()) {
                                view.populateMap(caches);
                            }
                        }, throwable -> {
                            // TODO Dialog
                        })
        );
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
