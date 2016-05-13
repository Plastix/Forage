package io.github.plastix.forage.ui.cachedetail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.State;
import io.github.plastix.forage.ForageApplication;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.PresenterActivity;
import io.github.plastix.forage.ui.compass.CompassActivity;
import io.github.plastix.forage.util.ActivityUtils;

/**
 * Activity that represents the Geocache detail view screen of the app.
 */
public class CacheDetailActivity extends PresenterActivity<CacheDetailPresenter, CacheDetailView> implements CacheDetailView {

    private static final String EXTRA_CACHE_CODE = "CACHE_CODE";
    private final static String BUNDLE_KEY_MAP_STATE = "BUNDLE_KEY_MAP_STATE";

    @BindView(R.id.cachedetail_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.cachedetail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.cachedetail_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.cachedetail_fab)
    FloatingActionButton fab;

    @BindView(R.id.cachedetail_map)
    MapView map;

    @BindView(R.id.cachedetail_description)
    TextView description;

    @BindView(R.id.cachedetail_difficulty)
    TextView difficulty;

    @BindView(R.id.cachedetail_size)
    TextView size;

    @BindView(R.id.cachedetail_terrain)
    TextView terrain;

    @BindView(R.id.cachedetail_title)
    TextView title;

    @BindView(R.id.cachedetail_type)
    TextView type;

    @Inject
    Resources resources;

    @State
    String cacheCode;

    @State
    String cacheName;

    /**
     * Static factory method that returns a new intent for opening a {@link CacheDetailActivity}.
     *
     * @param context   A context
     * @param cacheCode Cache code
     * @return A new intent.
     */
    public static Intent newIntent(Context context, String cacheCode) {
        Intent intent = new Intent(context, CacheDetailActivity.class);
        intent.putExtra(EXTRA_CACHE_CODE, cacheCode);

        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_detail);

        this.cacheCode = getIntent().getStringExtra(EXTRA_CACHE_CODE);

        initializeView(savedInstanceState);

    }

    private void injectDependencies() {
        ForageApplication.getComponent(this)
                .plus(new CacheDetailModule(this)).injectTo(this);
    }

    private void initializeView(Bundle savedInstanceState) {
        setActivityActionBar();

        Bundle mapState = null;
        if (savedInstanceState != null) {
            // Load the map state bundle from the main savedInstanceState
            mapState = savedInstanceState.getBundle(BUNDLE_KEY_MAP_STATE);
        }

        map.onCreate(mapState);
    }

    private void setActivityActionBar() {
        setSupportActionBar(toolbar);

        ActivityUtils.setSupportActionBarBack(getDelegate());
    }

    @Override
    protected void onPresenterPrepared(CacheDetailPresenter presenter) {
        presenter.getGeocache(cacheCode);
    }

    @Override
    public void returnedGeocache(final Cache cache) {
        title.setText(cache.name);
        type.setText(resources.getString(R.string.cacheitem_type, cache.type));
        description.setText(cache.description);
        difficulty.setText(resources.getString(R.string.cachedetail_rating, cache.difficulty));
        terrain.setText(resources.getString(R.string.cachedetail_rating, cache.terrain));
        size.setText(cache.size);

        setFabClickListener(cache);

        MapListener mapListener = new MapListener(cache.location.toLocation());
        map.getMapAsync(mapListener);
    }

    public void setFabClickListener(final Cache cache) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CompassActivity.newIntent(CacheDetailActivity.this, cache.location.toLocation());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onError() {
        finish();
    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Bundle mapState = new Bundle();
        map.onSaveInstanceState(mapState);
        // Put the map bundle in the main outState
        outState.putBundle(BUNDLE_KEY_MAP_STATE, mapState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Class to encapsulate Google Map config logic.
     */
    private class MapListener implements OnMapReadyCallback {

        private Location location;

        public MapListener(Location location) {
            this.location = location;
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            // Add marker for geocache and move camera
            LatLng markerPos = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(markerPos));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerPos)
                    .zoom(13).build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            animateIntoView();
        }

        private void animateIntoView() {
            map.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    map.setVisibility(View.VISIBLE);
                }
            }).alpha(1f)
                    .setDuration(500);
        }

    }

}



