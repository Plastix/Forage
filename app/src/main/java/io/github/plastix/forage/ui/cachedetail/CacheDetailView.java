package io.github.plastix.forage.ui.cachedetail;

import io.github.plastix.forage.data.local.Cache;

/**
 * Interface implemented by {@link CacheDetailFragment} to define callbacks used by
 * {@link CacheDetailPresenter}.
 */
public interface CacheDetailView {

    void returnedGeocache(Cache cache);

    void onError();
}
