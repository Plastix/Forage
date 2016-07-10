package io.github.plastix.forage.ui.login;

import android.net.Uri;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;

public class LoginPresenter extends RxPresenter<LoginView> {

    private OAuthInteractor oAuthInteractor;

    @Inject
    public LoginPresenter(OAuthInteractor oAuthInteractor) {
        this.oAuthInteractor = oAuthInteractor;
    }

    public void startOAuth() {
        addSubscription(
                oAuthInteractor.retrieveRequestToken()
                        .toObservable()
                        .compose(this.<String>deliverFirst())
                        .toSingle()
                        .subscribe(s -> {
                                    if (isViewAttached()) {
                                        view.openBrowser(s);
                                    }
                                },
                                throwable -> {
                                    if (isViewAttached()) {
                                        view.onErrorRequestToken();
                                    }
                                })
        );
    }

    public void oauthCallback(final Uri uri) {
        addSubscription(
                oAuthInteractor.retrieveAccessToken(uri)
                        .toObservable()
                        .compose(deliverFirst())
                        .toCompletable()
                        .subscribe(() -> {
                            if (isViewAttached()) {
                                view.onAuthSuccess();
                            }
                        }, throwable -> {
                            if (isViewAttached()) {
                                view.onErrorAccessToken();
                            }
                        })
        );
    }

    @Override
    public void onDestroyed() {
        oAuthInteractor = null;
    }
}
