package io.github.plastix.forage.ui.cachelist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.plastix.forage.R;

public class CacheListFragment extends Fragment implements CacheListView {

    @Inject
    CacheListPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();
    }

    private void injectDependencies() {
        DaggerCacheListComponent.builder()
                .cacheListModule(new CacheListModule(this))
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cache_list, container, false);
    }

}
