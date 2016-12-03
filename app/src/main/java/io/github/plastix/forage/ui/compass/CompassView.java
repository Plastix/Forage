package io.github.plastix.forage.ui.compass;

/**
 * Interface implemented by {@link CompassActivity} to define callbacks used by
 * {@link CompassPresenter}.
 */
public interface CompassView {

    void rotateCompass(float degrees);

    void setDistance(float distanceInMeters);

    void setAccuracy(float accuracyInMeters);

    void showLocationUnavailableDialog();

    void showCompass();

}
