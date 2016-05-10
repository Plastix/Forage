package io.github.plastix.forage.ui.login;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private LoginView loginView;

    public LoginModule(LoginView loginView) {
        this.loginView = loginView;
    }

    @Provides
    public LoginView provideLoginView(){
        return loginView;
    }
}
