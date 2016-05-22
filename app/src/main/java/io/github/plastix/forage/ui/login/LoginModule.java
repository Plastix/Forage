package io.github.plastix.forage.ui.login;

import dagger.Module;
import io.github.plastix.forage.ui.base.BaseModule;

@Module
public class LoginModule extends BaseModule<LoginView> {
    public LoginModule(LoginView view) {
        super(view);
    }
}
