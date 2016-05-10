package io.github.plastix.forage.ui.login;

import android.net.Uri;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.ui.Presenter;
import rx.SingleSubscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class LoginPresenter extends Presenter<LoginView> {

    private OAuthInteractor oAuthInteractor;

    @Inject
    public LoginPresenter(OAuthInteractor oAuthInteractor) {
        this.oAuthInteractor = oAuthInteractor;
    }

    public void startOAuth() {
        oAuthInteractor.retrieveRequestToken().subscribe(new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                view.openBrowser(value);
            }

            @Override
            public void onError(Throwable error) {
                view.onErrorRequestToken();
            }
        });


    }

    public void oauthCallback(final Uri uri) {
        oAuthInteractor.retrieveAccessToken(uri)
                .subscribe(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onErrorAccessToken();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        view.onAuthSuccess();
                    }
                });
    }

}
