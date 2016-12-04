package io.github.plastix.forage.ui.cachedetail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.State;
import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.base.PresenterActivity;
import io.github.plastix.forage.ui.compass.CompassActivity;
import io.github.plastix.forage.ui.log.LogActivity;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.MenuUtils;

/**
 * Activity that represents the Geocache detail view screen of the app.
 */
public class CacheDetailActivity extends PresenterActivity<CacheDetailPresenter, CacheDetailView> implements CacheDetailView {

    private static final String EXTRA_CACHE_CODE = "CACHE_CODE";

    @BindView(R.id.cachdetail_coordinatorlayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.cachedetail_appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.cachedetail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.cachedetail_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.cachedetail_fab)
    FloatingActionButton fab;

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

    private SupportMapFragment map;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_detail);

        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());

        this.cacheCode = getIntent().getStringExtra(EXTRA_CACHE_CODE);
        this.map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.cachedetail_map);
    }

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        component.plus(new CacheDetailModule(this))
                .injectTo(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getGeocache(cacheCode);
    }

    @Override
    public void returnedGeocache(final Cache cache) {
        title.setText(cache.name);
        type.setText(resources.getString(R.string.cacheitem_type, cache.type));
        description.setText(cache.description);
        difficulty.setText(resources.getString(R.string.cachedetail_rating_difficulty, cache.difficulty));
        terrain.setText(resources.getString(R.string.cachedetail_rating_terrain, cache.terrain));
        size.setText(cache.size);

        setFabClickListener(cache);

        MapListener mapListener = new MapListener(cache.location.toLocation());
        map.getMapAsync(mapListener);
    }

    public void setFabClickListener(final Cache cache) {
        fab.setOnClickListener(v -> {
            Intent intent = CompassActivity.newIntent(CacheDetailActivity.this, cache.location.toLocation());
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cache_detail, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        @ColorInt int color = ContextCompat.getColor(this, R.color.white);
        MenuUtils.tintMenuItemIcon(color, menu.findItem(R.id.cachedetail_action_log));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cachedetail_action_log:
                presenter.openLogScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onError() {
        finish();
    }

    @Override
    public void openLogScreen() {
        startActivity(LogActivity.newIntent(this, cacheCode));
    }

    @Override
    public void onErrorRequiresLogin() {
        Snackbar.make(collapsingToolbarLayout, R.string.cachedetail_error_requires_login, Snackbar.LENGTH_LONG).show();
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

        }
    }

}



