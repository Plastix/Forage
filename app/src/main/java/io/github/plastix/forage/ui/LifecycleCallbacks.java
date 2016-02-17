package io.github.plastix.forage.ui;

/**
 * Android lifecycle callbacks from Activities/Fragments. Presenters can implement this interface to
 * link to lifecycle callbacks.
 */
public interface LifecycleCallbacks {

    void onStart();

    void onStop();

    void onResume();

    void onPause();

}
