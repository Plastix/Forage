package io.github.plastix.forage.ui.cachedetail;

import io.github.plastix.forage.data.local.model.Cache;

/**
 * Interface implemented by {@link CacheDetailActivity} to define callbacks used by
 * {@link CacheDetailPresenter}.
 */
public interface CacheDetailView {

    void returnedGeocache(Cache cache);

    void onError();

    void openLogScreen();

    void onErrorRequiresLogin();
}
