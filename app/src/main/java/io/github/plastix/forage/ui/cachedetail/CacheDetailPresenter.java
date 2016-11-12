package io.github.plastix.forage.ui.cachedetail;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.github.plastix.rxdelay.RxDelay;

public class CacheDetailPresenter extends RxPresenter<CacheDetailView> {

    private DatabaseInteractor databaseInteractor;
    private OAuthInteractor oAuthInteractor;

    @Inject
    public CacheDetailPresenter(DatabaseInteractor databaseInteractor, OAuthInteractor oAuthInteractor) {
        this.databaseInteractor = databaseInteractor;
        this.oAuthInteractor = oAuthInteractor;
    }

    public void getGeocache(String cacheCode) {
        addSubscription(databaseInteractor.getGeocacheCopy(cacheCode)
                .toObservable()
                .compose(RxDelay.delayFirst(getViewState()))
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

    public void openLogScreen() {
        if (oAuthInteractor.hasSavedOAuthTokens()) {
            if (isViewAttached()) {
                view.openLogScreen();
            }
        } else {
            if (isViewAttached()) {
                view.onErrorRequiresLogin();
            }
        }
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
