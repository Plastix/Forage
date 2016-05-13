package io.github.plastix.forage.ui.cachelist;

/**
 * Interface implemented by {@link CacheListFragment} to define callbacks used by
 * {@link CacheListPresenter}.
 */
public interface CacheListView {

    void onErrorInternet();

    void onErrorFetch();

    void onErrorLocation();

    void setRefreshing();

}
