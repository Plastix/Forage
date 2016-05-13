package io.github.plastix.forage.ui.navigate;

public interface NavigateView {

    void errorParsingLatitude();

    void errorParsingLongitude();

    void openCompassScreen(double lat, double lon);
}
