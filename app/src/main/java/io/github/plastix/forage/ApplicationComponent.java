package io.github.plastix.forage;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.github.plastix.forage.ui.cachelist.CacheListActivity;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
}
