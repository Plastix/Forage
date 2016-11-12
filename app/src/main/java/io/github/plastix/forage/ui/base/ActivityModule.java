package io.github.plastix.forage.ui.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ActivityModule {

    private final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    public Context provideContext() {
        return activity.getBaseContext();
    }
}
