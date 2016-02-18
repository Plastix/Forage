package io.github.plastix.forage.ui.compass;

import io.github.plastix.forage.ui.View;

/**
 * Interface implemented by {@link CompassFragment} to define callbacks used by
 * {@link CompassPresenter}.
 */
public interface CompassView extends View {

    void rotateCompass(Float degrees);

    void updateDistance(double distance);

}
