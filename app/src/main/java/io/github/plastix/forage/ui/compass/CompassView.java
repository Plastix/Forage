package io.github.plastix.forage.ui.compass;

/**
 * Interface implemented by {@link CompassFragment} to define callbacks used by
 * {@link CompassPresenter}.
 */
public interface CompassView {

    void rotateCompass(Float degrees);

}
