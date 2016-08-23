package io.github.plastix.forage.ui.cachelist;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.PresenterFragment;
import io.github.plastix.forage.ui.misc.PermissionRationaleDialog;
import io.github.plastix.forage.ui.misc.SimpleDividerItemDecoration;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.PermissionUtils;

/**
 * Fragment that is responsible for the Geocache list.
 */
public class CacheListFragment extends PresenterFragment<CacheListPresenter, CacheListView> implements CacheListView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int LOCATION_REQUEST_CODE = 0;
    private static final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;

    @Inject
    CacheAdapter adapter;

    @Inject
    LinearLayoutManager linearLayoutManager;

    @Inject
    SimpleDividerItemDecoration itemDecorator;

    @BindView(R.id.cachelist_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.cachelist_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    View emptyView;

    private DataChangeListener dataChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new CacheListModule(this))
                .injectTo(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cache_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityUtils.setSupportActionBarTitle(getActivity(), R.string.cachelist_screen_title);
        setupRecyclerView();
        setupSwipeRefresh();
        updateEmptyView();
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(itemDecorator);

        dataChangeListener = new DataChangeListener(this);
        adapter.registerAdapterDataObserver(dataChangeListener);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void updateEmptyView() {
        if (recyclerView.getAdapter() == null || adapter.getItemCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

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

    private void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
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
        presenter.getGeocachesFromInternet();
    }

    @Override
    public void setRefreshing() {
        // TODO: Post fix required due to Support V4 bug
        // Will be fixed in 24.2.0
        // See https://code.google.com/p/android/issues/detail?id=77712
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        }
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

        private final CacheListFragment fragment;

        public DataChangeListener(CacheListFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onChanged() {
            fragment.stopRefresh();
            fragment.updateEmptyView();

        }
    }


}
