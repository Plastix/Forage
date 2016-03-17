package io.github.plastix.forage.ui.cachedetail;

import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.View;

/**
 * Interface implemented by {@link CacheDetailActivity} to define callbacks used by
 * {@link CacheDetailPresenter}.
 */
public interface CacheDetailView extends View {

    void returnedGeocache(Cache cache);

    void onError();
}
