package io.github.plastix.forage.ui;

/**
 * Android lifecycle callbacks from activities/fragments. Presenters can implement this interface to
 * link to lifecycle callbacks. (Optional interface)
 */
public interface LifecycleCallbacks {

    void onStart();
    void onStop();
    void onResume();
}
