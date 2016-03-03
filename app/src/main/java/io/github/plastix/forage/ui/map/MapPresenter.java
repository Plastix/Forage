package io.github.plastix.forage.ui.map;

import java.util.List;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.Cache;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.ui.Presenter;
import rx.SingleSubscriber;

public class MapPresenter extends Presenter<MapFragView> {

    private DatabaseInteractor databaseInteractor;

    @Inject
    public MapPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocaches() {
        databaseInteractor.getGeocaches().subscribe(new SingleSubscriber<List<Cache>>() {
            @Override
            public void onSuccess(List<Cache> value) {
                view.populateMap(value);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseInteractor.onDestroy();
    }
}
