package io.github.plastix.forage.ui.login;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.forage.ui.base.ActivityModule;

@Module
public class LoginModule extends ActivityModule {
    public LoginModule(AppCompatActivity activity) {
        super(activity);
    }
}
