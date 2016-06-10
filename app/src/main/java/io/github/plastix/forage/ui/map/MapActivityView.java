package io.github.plastix.forage.ui.map;

import android.location.Location;

import java.util.List;

import io.github.plastix.forage.data.local.model.Cache;

/**
 * Interface implemented by {@link MapActivity} to define callbacks used by
 * {@link MapPresenter}.
 */
public interface MapActivityView {

    void addMapMarkers(List<Cache> caches);

    void animateMapCamera(Location location);
}
