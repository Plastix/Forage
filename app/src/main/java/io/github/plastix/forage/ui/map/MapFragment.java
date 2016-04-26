package io.github.plastix.forage.ui.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.PresenterFragment;

/**
 * Fragment that is responsible for the geocache map.
 */
public class MapFragment extends PresenterFragment<MapPresenter> implements MapFragView, OnMapReadyCallback {

    @BindView(R.id.map_mapview)
    MapView map;

    private GoogleMap googleMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
    }


    private void injectDependencies() {
        ForageApplication.getComponent(getContext())
                .plus(new MapModule(this))
                .injectTo(this);
    }

    @Override
    public void populateMap(List<Cache> caches) {
        if (googleMap != null) {
            for (Cache cache : caches) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(cache.location.latitude, cache.location.longitude))
                        .title(cache.name);
                googleMap.addMarker(markerOptions);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        this.googleMap = googleMap;
        presenter.getGeocaches();
    }

    @Override
    public void onDestroyView() {
        map.onDestroy();
        this.googleMap = null;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_map;
    }

}
