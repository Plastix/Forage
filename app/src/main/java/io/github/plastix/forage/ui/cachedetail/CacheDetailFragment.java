package io.github.plastix.forage.ui.cachedetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.Cache;


public class CacheDetailFragment extends Fragment implements CacheDetailView {

    @Inject
    CacheDetailPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        injectDependencies();
    }

    private void injectDependencies() {
        ForageApplication.getComponent(getContext())
                .plus(new CacheDetailModule(this)).injectTo(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cache_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Query presenter for cache
        presenter.getGeocache("hello");
    }

    @Override
    public void populateViews(Cache cache) {
        Log.d(getTag(), "Got cache! " + cache.getCode());
    }

    @Override
    public void onError() {
        Log.d(getTag(), "error getting cache!");
    }
}
