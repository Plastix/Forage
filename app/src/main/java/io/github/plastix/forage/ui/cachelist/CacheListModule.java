package io.github.plastix.forage.ui.cachelist;

import android.content.res.Resources;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class CacheListModule {

    private CacheListView cacheListView;

    public CacheListModule(CacheListView cacheListView) {
        this.cacheListView = cacheListView;
    }

    @Provides
    public CacheListView provideCacheListView() {
        return cacheListView;
    }

    @Provides
    public CacheAdapter provideCacheAdapter(Realm realm, Resources resources) {
        return new CacheAdapter(realm, resources);
    }

}
