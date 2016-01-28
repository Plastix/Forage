package io.github.plastix.forage.ui.cachedetail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.github.plastix.forage.R;
import io.github.plastix.forage.ui.BaseRetainedFragmentActivity;

public class CacheDetailActivity extends BaseRetainedFragmentActivity<CacheDetailFragment> {

    private static final String CACHE_DETAIL_FRAG = "io.github.plastix.forage.ui.cachelist.cachedetailfragment";
    private static final int CACHE_DETAIL_FRAME_ID = R.id.cachedetail_content_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cachedetail_toolbar);
        setSupportActionBar(toolbar);
    }

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



