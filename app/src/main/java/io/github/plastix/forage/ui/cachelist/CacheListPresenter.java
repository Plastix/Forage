package io.github.plastix.forage.ui.cachelist;

import javax.inject.Inject;

public class CacheListPresenter {

    private CacheListView view;

    @Inject
    public CacheListPresenter(CacheListView view) {
        this.view = view;
    }


}
