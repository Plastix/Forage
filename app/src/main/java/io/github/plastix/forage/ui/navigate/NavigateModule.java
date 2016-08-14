package io.github.plastix.forage.ui.navigate;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.forage.ui.base.ActivityModule;

@Module
public class NavigateModule extends ActivityModule {
    public NavigateModule(AppCompatActivity activity) {
        super(activity);
    }
}
