package io.github.plastix.forage.ui.login;

import io.github.plastix.forage.ui.View;

public interface LoginView extends View {

    void openBrowser(String authUrl);

    void onErrorRequestToken();

    void onErrorAccessToken();

    void onAuthSuccess();
}
