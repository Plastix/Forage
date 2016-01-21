package io.github.plastix.forage.ui.cachelist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.github.plastix.forage.R;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.PermissionUtils;

public class CacheListActivity extends AppCompatActivity {

    private static final String CACHE_LIST_FRAG = "io.github.plastix.forage.ui.cachelist.cachelistfragment";
    private static final int LOCATION_REQUEST_CODE = 0;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private FragmentManager fragmentManager;
    private CacheListFragment cacheFragment;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.cachelist_toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        addCacheListFragment();
    }

    private void addCacheListFragment() {
        cacheFragment = (CacheListFragment) fragmentManager.findFragmentByTag(CACHE_LIST_FRAG);

        if (cacheFragment == null) {

            // Create a new Fragment to be placed in the activity layout
            cacheFragment = new CacheListFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            cacheFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentManager.beginTransaction().replace(R.id.cachelist_content_frame, cacheFragment, CACHE_LIST_FRAG).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionUtils.requestPermissions(this, LOCATION_REQUEST_CODE, LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (!PermissionUtils.hasAllPermissionsGranted(grantResults)) {
                    showPermissionDialog();
                }
            }
        }
    }

    private void showPermissionDialog() {
        if (dialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
            dialogBuilder.setMessage(R.string.cachelist_nolocation);
            dialogBuilder.setNegativeButton(R.string.cachelist_exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            final Intent settingsIntent = ActivityUtils.getApplicationSettingsIntent(this);
            dialogBuilder.setPositiveButton(R.string.cachelist_open_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(settingsIntent);
                }
            });
            dialogBuilder.setCancelable(false);
            this.dialog = dialogBuilder.create();

        }

        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cache_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
