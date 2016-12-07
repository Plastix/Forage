package io.github.plastix.forage.ui.login;

import android.net.Uri;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.base.RxPresenter;
import io.github.plastix.rxdelay.RxDelay;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class LoginPresenter extends RxPresenter<LoginView> {

    private OAuthInteractor oAuthInteractor;
    private NetworkInteractor networkInteractor;

    private Subscription requestTokenSubscription = Subscriptions.unsubscribed();
    private Subscription accessTokenSubscription = Subscriptions.unsubscribed();

    @Inject
    public LoginPresenter(OAuthInteractor oAuthInteractor,
                          NetworkInteractor networkInteractor) {
        this.oAuthInteractor = oAuthInteractor;
        this.networkInteractor = networkInteractor;
    }

    public void startOAuth() {
        networkInteractor.hasInternetConnectionCompletable()
                .subscribe(this::getRequestToken, throwable -> {
                    if (isViewAttached()) {
                        view.onErrorNoInternet();
                    }
                });

    }

    private void getRequestToken() {
        requestTokenSubscription = oAuthInteractor.retrieveRequestToken()
                .compose(RxDelay.delaySingle(getViewState()))
                .doOnSubscribe(() -> {
                    if (isViewAttached()) {
                        view.showLoading();
                    }
                })
                .subscribe(s -> {
                            if (isViewAttached()) {
                                view.openBrowser(s);
                            }
                        },
                        throwable -> {
                            if (isViewAttached()) {
                                view.onErrorRequestToken();
                            }
                        });

        addSubscription(requestTokenSubscription);
    }

    public void oauthCallback(final Uri uri) {
        accessTokenSubscription = oAuthInteractor.retrieveAccessToken(uri)
                .compose(RxDelay.delayCompletable(getViewState()))
                .doOnSubscribe(subscription -> {
                    if (isViewAttached()) {
                        view.showLoading();
                    }
                })
                .subscribe(() -> {
                    if (isViewAttached()) {
                        view.onAuthSuccess();
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        view.onErrorAccessToken();
                    }
                });

        addSubscription(accessTokenSubscription);
    }

    @Override
    public void onViewAttached(LoginView view) {
        super.onViewAttached(view);

        if (!requestTokenSubscription.isUnsubscribed() || !accessTokenSubscription.isUnsubscribed()) {
            if (isViewAttached()) {
                view.showLoading();
            }
        }
    }

    @Override
    public void onDestroyed() {
        oAuthInteractor = null;
    }
}
