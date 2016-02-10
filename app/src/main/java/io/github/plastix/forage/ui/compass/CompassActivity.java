package io.github.plastix.forage.ui.compass;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.BaseRetainedFragmentActivity;
import io.github.plastix.forage.ui.cachelist.CacheListFragment;

/**
 * Activity that represents the compass screen of the app. This is a container activity
 * for {@link CompassFragment}.
 */
public class CompassActivity extends BaseRetainedFragmentActivity<CompassFragment> {

    private static final String COMPASS_FRAG = "io.github.plastix.forage.ui.compass.compassfragment";

    @IdRes
    private static final int COMPASS_FRAME_ID = R.id.compass_content_frame;

    @Bind(R.id.compass_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // Inject Butterknife views
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected String getFragmentTag() {
        return COMPASS_FRAG;
    }

    @Override
    protected CompassFragment getFragmentInstance() {
        return new CompassFragment();
    }

    @Override
    protected int getContainerViewId() {
        return COMPASS_FRAME_ID;
    }
}
