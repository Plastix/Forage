package io.github.plastix.forage.ui.login;

import android.net.Uri;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.ui.Presenter;
import rx.SingleSubscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class LoginPresenter extends Presenter<LoginView> {

    private OAuthInteractor oAuthInteractor;
    private CompositeSubscription subscriptions;

    @Inject
    public LoginPresenter(OAuthInteractor oAuthInteractor) {
        this.oAuthInteractor = oAuthInteractor;
        subscriptions = new CompositeSubscription();
    }

    public void startOAuth() {
        subscriptions.add(
                oAuthInteractor.retrieveRequestToken().subscribe(new SingleSubscriber<String>() {
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
        subscriptions.add(
                oAuthInteractor.retrieveAccessToken(uri)
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
    public void onViewDetached() {
        super.onViewDetached();
        subscriptions.clear();
    }

    @Override
    public void onDestroyed() {
        oAuthInteractor = null;
        subscriptions = null;
    }
}
