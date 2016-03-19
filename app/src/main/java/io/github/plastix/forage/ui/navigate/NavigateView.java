package io.github.plastix.forage.ui.navigate;

import android.location.Location;

import io.github.plastix.forage.ui.View;

public interface NavigateView extends View {

    void errorParsingLatitude();

    void errorParsingLongitude();

    void openCompassScreen(Location location);
}
