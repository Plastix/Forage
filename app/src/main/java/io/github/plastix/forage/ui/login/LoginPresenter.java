package io.github.plastix.forage.ui.login;

import android.net.Uri;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import rx.SingleSubscriber;
import rx.functions.Action0;
import rx.functions.Action1;

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
                        .subscribe(new SingleSubscriber<String>() {
                            @Override
                            public void onSuccess(String value) {
                                if (isViewAttached()) {
                                    view.openBrowser(value);
                                }
                            }

                            @Override
                            public void onError(Throwable error) {
                                if (isViewAttached()) {
                                    view.onErrorRequestToken();
                                }
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
                        .subscribe(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (isViewAttached()) {
                                    view.onErrorAccessToken();
                                }
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                if (isViewAttached()) {
                                    view.onAuthSuccess();
                                }
                            }
                        })
        );
    }

    @Override
    public void onDestroyed() {
        oAuthInteractor = null;
    }
}
