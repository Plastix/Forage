package io.github.plastix.forage.ui.cachedetail;

import io.github.plastix.forage.data.local.Cache;

public interface CacheDetailView {

    void populateViews(Cache cache);

    void onError();
}
