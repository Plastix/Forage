package io.github.plastix.forage.ui.cachelist;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.BaseFragmentActivity;
import io.github.plastix.forage.ui.login.LoginActivity;
import io.github.plastix.forage.ui.map.MapActivity;
import io.github.plastix.forage.ui.navigate.NavigateActivity;
import io.github.plastix.forage.util.ActivityUtils;
import io.github.plastix.forage.util.MenuUtils;
import io.github.plastix.forage.util.PermissionUtils;

/**
 * Activity that represents the main Geocache list screen of the app. This is a container activity
 * for {@link CacheListFragment}.
 */
public class CacheListActivity extends BaseFragmentActivity<CacheListFragment> {

    private static final String CACHE_LIST_FRAG = "io.github.plastix.forage.ui.cachelist.cachelistfragment";

    @IdRes
    private static final int CACHE_LIST_FRAME_ID = R.id.cachelist_content_frame;

    private static final int LOCATION_REQUEST_CODE = 0;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    @BindView(R.id.cachelist_toolbar)
    Toolbar toolbar;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_list);

        setSupportActionBar(toolbar);
    }

    /**
     * Request location permissions every time the Activity is started. This is important because
     * a user can disable the permission while the activity is running in the background and return
     * to the activity.
     */
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
                if (!PermissionUtils.isPermissionRequestCancelled(grantResults) && !PermissionUtils.hasAllPermissionsGranted(grantResults)) {
                    showPermissionRationaleDialog();
                }
            }
        }
    }

    private void showPermissionRationaleDialog() {
        buildDialog();

        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    private void buildDialog() {
        if (dialog == null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
            dialogBuilder.setMessage(R.string.cachelist_nolocation);
            dialogBuilder.setNegativeButton(R.string.cachelist_exit, (dialog1, which) -> {
                finish();
            });
            final Intent settingsIntent = ActivityUtils.getApplicationSettingsIntent(this);
            dialogBuilder.setPositiveButton(R.string.cachelist_open_settings, (dialog1, which) -> {
                startActivity(settingsIntent);
            });
            dialogBuilder.setCancelable(false);
            this.dialog = dialogBuilder.create();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissRationaleDialog();
    }

    private void dismissRationaleDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cache_list, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        @ColorInt int color = ContextCompat.getColor(this, R.color.white);
        MenuUtils.tintMenuItemIcon(color, menu.findItem(R.id.cachelist_action_map));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cachelist_action_map:
                startActivity(MapActivity.newIntent(this));
                return true;
            case R.id.cachelist_action_navigate:
                startActivity(NavigateActivity.newIntent(this));
                return true;
            case R.id.cachelist_action_login:
                startActivity(LoginActivity.newIntent(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected String getFragmentTag() {
        return CACHE_LIST_FRAG;
    }

    @Override
    protected CacheListFragment getFragmentInstance() {
        return new CacheListFragment();
    }

    @Override
    protected int getContainerViewId() {
        return CACHE_LIST_FRAME_ID;
    }
}
