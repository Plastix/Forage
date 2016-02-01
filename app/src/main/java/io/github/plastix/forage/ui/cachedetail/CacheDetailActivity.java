package io.github.plastix.forage.ui.cachedetail;

import android.os.Bundle;
import android.support.annotation.IdRes;

import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.BaseRetainedFragmentActivity;

/**
 * Activity that represents the Geocache detail view screen of the app. This is a container activity
 * for {@link CacheDetailFragment}.
 */
public class CacheDetailActivity extends BaseRetainedFragmentActivity<CacheDetailFragment> {

    private static final String CACHE_DETAIL_FRAG = "io.github.plastix.forage.ui.cachelist.cachedetailfragment";

    @IdRes
    private static final int CACHE_DETAIL_FRAME_ID = R.id.cachedetail_content_frame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_detail);
    }

    // Overloaded methods from BaseRetainedFragmentActivity
    // Used to automatically attach the correct retained fragment to the activity

    @Override
    protected String getFragmentTag() {
        return CACHE_DETAIL_FRAG;
    }

    @Override
    protected CacheDetailFragment getFragmentInstance() {
        return new CacheDetailFragment();
    }

    @Override
    protected int getContainerViewId() {
        return CACHE_DETAIL_FRAME_ID;
    }
}



