package io.github.plastix.forage.ui.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.base.BaseFragmentActivity;
import io.github.plastix.forage.util.ActivityUtils;

/**
 * Activity that represents the map screen of the app. This is a container activity
 * for {@link MapFragment}.
 */
public class MapActivity extends BaseFragmentActivity<MapFragment> {

    private static final String MAP_FRAG = "io.github.plastix.forage.ui.map.mapfragment";

    @IdRes
    private static final int MAP_FRAME_ID = R.id.map_content_frame;

    @BindView(R.id.map_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setSupportActionBar(toolbar);
        ActivityUtils.setSupportActionBarBack(getDelegate());
    }

    /**
     * Static factory method that returns a new intent for opening a {@link MapActivity}.
     *
     * @param context A context
     * @return A new intent.
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        return intent;
    }

    @Override
    protected String getFragmentTag() {
        return MAP_FRAG;
    }

    @Override
    protected MapFragment getFragmentInstance() {
        return new MapFragment();
    }

    @Override
    protected int getContainerViewId() {
        return MAP_FRAME_ID;
    }
}
