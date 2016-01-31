package io.github.plastix.forage.ui.cachedetail;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.Cache;


/**
 * Fragment that is responsible for showing the Geocache detail UI.
 */
public class CacheDetailFragment extends Fragment implements CacheDetailView {

    private static final String EXTRA_CACHE_CODE = "CACHE_CODE";

    @Bind(R.id.cachedetail_toolbar)
    Toolbar toolbar;

    @Bind(R.id.cachedetail_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.cachedetail_fab)
    FloatingActionButton fab;

    @Bind(R.id.cachedetail_description)
    TextView description;

    @Bind(R.id.cachedetail_type)
    TextView type;

    @Bind(R.id.cachedetail_map)
    MapView map;

    @Inject
    CacheDetailPresenter presenter;

    private String cacheCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        this.cacheCode = getArguments().getString(EXTRA_CACHE_CODE);
        ;

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
        View view = inflater.inflate(R.layout.fragment_cache_detail, container, false);

        // Inject Butterknife views
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setActivityActionBar();

        setFab();

        // TODO Hacky fix for Google Maps
        map.onCreate(null);
        map.setVisibility(View.GONE);

        presenter.getGeocache(cacheCode);
    }

    private void setActivityActionBar() {
        AppCompatActivity parent = ((AppCompatActivity) getActivity());
        parent.setSupportActionBar(toolbar);

        parent.getDelegate().getSupportActionBar().setDisplayShowHomeEnabled(true);
        parent.getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFab() {
        fab.setImageDrawable(new IconicsDrawable(getContext(), CommunityMaterial.Icon.cmd_navigation).color(Color.WHITE));
        // TODO Fab clicks
    }

    @Override
    public void returnedGeocache(Cache cache) {
        collapsingToolbarLayout.setTitle(cache.getName());
        description.setText(Html.fromHtml(cache.getDescription()).toString());

        type.setText(cache.getType());

        MapBuilder mapBuilder = new MapBuilder(cache.getLocation());
        map.getMapAsync(mapBuilder);
    }

    @Override
    public void onError() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(getActivity());
    }

    private class MapBuilder implements OnMapReadyCallback {

        private double lat;
        private double lon;

        public MapBuilder(String location) {
            final String[] parts = location.split("\\|");
            this.lat = Double.parseDouble(parts[0]);
            this.lon = Double.parseDouble(parts[1]);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            // Add marker for geocache and move camera
            LatLng markerPos = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions()
                    .position(markerPos));

            CameraPosition camerPosition = new CameraPosition.Builder()
                    .target(markerPos)
                    .zoom(13).build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerPosition));

            animateView();
        }

        private void animateView() {
            map.setVisibility(View.VISIBLE);
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            map.startAnimation(animation);
        }

    }

}
