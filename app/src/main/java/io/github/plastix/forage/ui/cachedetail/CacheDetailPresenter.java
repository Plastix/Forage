package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Inject;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.Presenter;
import io.github.plastix.forage.util.RxUtils;
import rx.SingleSubscriber;
import rx.Subscription;

public class CacheDetailPresenter extends Presenter<CacheDetailView> {

    private DatabaseInteractor databaseInteractor;
    private Subscription subscription;

    @Inject
    public CacheDetailPresenter(DatabaseInteractor databaseInteractor) {
        this.databaseInteractor = databaseInteractor;
    }

    public void getGeocache(String cacheCode) {
        subscription = databaseInteractor.getGeocacheCopy(cacheCode)
                .subscribe(new SingleSubscriber<Cache>() {
                    @Override
                    public void onSuccess(Cache value) {
                        if (isViewAttached()) {
                            view.returnedGeocache(value);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        if (isViewAttached()) {
                            view.onError();
                        }
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
