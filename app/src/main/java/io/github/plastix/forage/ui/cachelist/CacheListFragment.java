package io.github.plastix.forage.ui.cachelist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.PresenterFragment;
import io.github.plastix.forage.ui.misc.PermissionRationaleDialog;
import io.github.plastix.forage.ui.misc.SimpleDividerItemDecoration;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.PermissionUtils;
import io.realm.OrderedRealmCollection;

/**
 * Fragment that is responsible for the Geocache list.
 */
public class CacheListFragment extends PresenterFragment<CacheListPresenter, CacheListView> implements CacheListView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int LOCATION_REQUEST_CODE = 0;
    private static final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;

    @Inject
    SimpleDividerItemDecoration itemDecorator;

    @BindView(R.id.cachelist_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.cachelist_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;

    private CacheAdapter adapter;
    private DataChangeListener dataChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
    }

    private void injectDependencies() {
        ForageApplication.getComponent(getContext())
                .plus(new CacheListModule(this)).injectTo(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cache_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityUtils.setSupportActionBarTitle(getActivity(), R.string.cachelist_title);

        adapter = new CacheAdapter(getContext(), null, true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(itemDecorator);

        this.dataChangeListener = new DataChangeListener(this);
        adapter.registerAdapterDataObserver(dataChangeListener);

        swipeRefreshLayout.setOnRefreshListener(this);
        updateEmptyView();
    }

    private void updateEmptyView() {
        stopRefresh();
        if (recyclerView.getAdapter() == null || adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

    }

    private void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getGeocachesFromDatabase();
    }

    @Override
    public void setGeocacheList(OrderedRealmCollection<Cache> caches) {
        adapter.updateData(caches);
    }

    @Override
    public void setRefreshing() {
        // TODO: Post fix required due to Support V4 bug
        // Will be fixed in 24.1.0
        // See https://code.google.com/p/android/issues/detail?id=77712
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void onErrorInternet() {
        makeErrorSnackbar(R.string.cachelist_error_no_internet);
    }

    private void makeErrorSnackbar(@StringRes int resID) {
        stopRefresh();
        Snackbar.make(recyclerView, resID, Snackbar.LENGTH_LONG)
                .setAction(R.string.cachelist_retry, v -> {
                    onRefresh();
                }).show();
    }

    /**
     * SwipeRefreshView callback.
     */
    @Override
    public void onRefresh() {
        if (PermissionUtils.hasPermission(getContext(), LOCATION_PERMISSION)) {
            downloadGeocaches();
        } else {
            requestPermissions(new String[]{LOCATION_PERMISSION}, LOCATION_REQUEST_CODE);
        }
    }

    private void downloadGeocaches() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.getGeocachesFromInternet();
    }

    @Override
    public void onErrorFetch() {
        makeErrorSnackbar(R.string.cachelist_error_failed_parse);
    }

    @Override
    public void onErrorLocation() {
        makeErrorSnackbar(R.string.cachelist_error_no_location);
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        adapter.unregisterAdapterDataObserver(dataChangeListener);
        dataChangeListener = null;

        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (PermissionUtils.hasAllPermissionsGranted(grantResults)) {
                    downloadGeocaches();
                } else {
                    PermissionRationaleDialog.show(getActivity(), R.string.cachelist_nolocation);
                }
            }
        }
    }

    private static class DataChangeListener extends RecyclerView.AdapterDataObserver {

        private final WeakReference<CacheListFragment> fragmentWeakReference;

        public DataChangeListener(CacheListFragment fragmentWeakReference) {
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        public void onChanged() {
            super.onChanged();
            CacheListFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                fragment.updateEmptyView();
            }

        }
    }


}
