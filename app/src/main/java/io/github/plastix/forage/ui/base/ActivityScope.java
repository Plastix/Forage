package io.github.plastix.forage.ui.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Custom Dagger 2 scope for Activities/Fragments
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}