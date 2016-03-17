package io.github.plastix.forage.ui.map;

import java.util.List;

import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.View;

/**
 * Interface implemented by {@link MapFragment} to define callbacks used by
 * {@link MapPresenter}.
 */
public interface MapFragView extends View {

    void populateMap(List<Cache> caches);
}
