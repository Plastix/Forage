package io.github.plastix.forage.ui.log;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.forage.ui.base.ActivityModule;

@Module
public class LogModule extends ActivityModule {
    public LogModule(AppCompatActivity activity) {
        super(activity);
    }
}
