package io.github.plastix.forage.ui.login;

import dagger.Subcomponent;
import io.github.plastix.forage.ui.base.ActivityScope;

@ActivityScope
@Subcomponent(
        modules = {
                LoginModule.class
        }
)
public interface LoginComponent {
    void injectTo(LoginActivity loginActivity);
}
