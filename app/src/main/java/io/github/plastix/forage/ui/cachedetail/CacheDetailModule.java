package io.github.plastix.forage.ui.cachedetail;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import io.github.plastix.forage.ui.base.ActivityModule;

/**
 * Dagger module that provides dependencies for {@link CacheDetailPresenter} and {@link CacheDetailActivity}.
 */
@Module
public class CacheDetailModule extends ActivityModule {
    public CacheDetailModule(AppCompatActivity activity) {
        super(activity);
    }
}

