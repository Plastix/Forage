package io.github.plastix.forage.ui.cachelist;

import io.github.plastix.forage.ui.View;

/**
 * Interface implemented by {@link CacheListFragment} to define callbacks used by
 * {@link CacheListPresenter}.
 */
public interface CacheListView extends View {

    void onErrorInternet();

    void onErrorFetch();

    void onErrorLocation();

    void setRefreshing();

}
