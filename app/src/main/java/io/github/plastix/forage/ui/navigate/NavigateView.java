package io.github.plastix.forage.ui.navigate;

import io.github.plastix.forage.ui.View;

public interface NavigateView extends View {

    void errorParsingLatitude();

    void errorParsingLongitude();

    void openCompassScreen(double lat, double lon);
}
