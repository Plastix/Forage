package io.github.plastix.forage.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.Presenter;
import io.github.plastix.forage.util.RxUtils;
import rx.SingleSubscriber;
import rx.Subscription;

public class MapPresenter extends Presenter<MapFragView> {

    private DatabaseInteractor databaseInteractor;
    private Subscription subscription;

    @Inject
    public MapPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocaches() {
        subscription = databaseInteractor.getGeocaches().subscribe(new SingleSubscriber<List<Cache>>() {
            @Override
            public void onSuccess(List<Cache> value) {
                if (isViewAttached()) {
                    view.populateMap(value);
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        RxUtils.safeUnsubscribe(subscription);
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
        subscription = null;
    }
}
