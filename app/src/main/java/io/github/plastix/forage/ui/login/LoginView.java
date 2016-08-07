package io.github.plastix.forage.ui.login;

public interface LoginView {

    void openBrowser(String authUrl);

    void onErrorRequestToken();

    void onErrorAccessToken();

    void onErrorNoInternet();

    void onAuthSuccess();

    void showLoading();

    void stopLoading();
}
