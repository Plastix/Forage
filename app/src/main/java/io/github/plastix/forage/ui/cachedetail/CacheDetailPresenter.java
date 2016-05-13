package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.Presenter;
import rx.SingleSubscriber;

public class CacheDetailPresenter extends Presenter<CacheDetailView> {

    private DatabaseInteractor databaseInteractor;

    @Inject
    public CacheDetailPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocache(String cacheCode) {
        databaseInteractor.getGeocache(cacheCode).subscribe(new SingleSubscriber<Cache>() {
            @Override
            public void onSuccess(Cache value) {
                view.returnedGeocache(value);
            }

            @Override
            public void onError(Throwable error) {
                view.onError();
            }
        });
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
