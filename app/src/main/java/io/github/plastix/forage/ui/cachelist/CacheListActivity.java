package io.github.plastix.forage.ui.cachelist;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.about.AboutActivity;
import io.github.plastix.forage.ui.base.BaseFragmentActivity;
import io.github.plastix.forage.ui.login.LoginActivity;
import io.github.plastix.forage.ui.map.MapActivity;
import io.github.plastix.forage.ui.navigate.NavigateActivity;
import io.github.plastix.forage.util.MenuUtils;

/**
 * Activity that represents the main Geocache list screen of the app. This is a container activity
 * for {@link CacheListFragment}.
 */
public class CacheListActivity extends BaseFragmentActivity<CacheListFragment> {

    private static final String CACHE_LIST_FRAG = "io.github.plastix.forage.ui.cachelist.cachelistfragment";

    @IdRes
    private static final int CACHE_LIST_FRAME_ID = R.id.cachelist_content_frame;

    @BindView(R.id.cachelist_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_list);

        setSupportActionBar(toolbar);
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
            case R.id.action_about:
                startActivity(AboutActivity.newIntent(this));
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

    @Override
    protected void injectDependencies(ApplicationComponent component) {
        // No injections
    }
}
