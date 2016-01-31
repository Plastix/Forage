package io.github.plastix.forage.ui.cachedetail;

import android.util.Log;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.Cache;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import rx.SingleSubscriber;

public class CacheDetailPresenter {

    private CacheDetailView view;
    private DatabaseInteractor databaseInteractor;

    @Inject
    public CacheDetailPresenter(CacheDetailView view, DatabaseInteractor databaseInteractor) {
        this.view = view;
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


}
