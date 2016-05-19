package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.rx.RxPresenter;

public class CacheDetailPresenter extends RxPresenter<CacheDetailView> {

    private DatabaseInteractor databaseInteractor;

    @Inject
    public CacheDetailPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocache(String cacheCode) {
        addSubscription(databaseInteractor.getGeocacheCopy(cacheCode)
                .toObservable()
                .compose(this.<Cache>deliverFirst())
                .toSingle()
                .subscribe(cache -> {
                    if (isViewAttached()) {
                        view.returnedGeocache(cache);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        view.onError();
                    }
                })
        );
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
