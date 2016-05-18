package io.github.plastix.forage.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.realm.OrderedRealmCollection;
import rx.SingleSubscriber;

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
                        .subscribe(new SingleSubscriber<List<Cache>>() {
                            @Override
                            public void onSuccess(List<Cache> value) {
                                if (isViewAttached()) {
                                    view.populateMap(value);
                                }
                            }

                            @Override
                            public void onError(Throwable error) {

                            }
                        })
        );
    }


    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
