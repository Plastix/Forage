package io.github.plastix.forage.ui.cachelist;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;

public class CacheListPresenter {

    private CacheListView view;
    private OkApiInteractor apiInteractor;

    @Inject
    public CacheListPresenter(CacheListView view, OkApiInteractor apiInteractor) {
        this.view = view;
        this.apiInteractor = apiInteractor;
    }


}
