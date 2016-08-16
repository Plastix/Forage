package io.github.plastix.forage.ui.cachelist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import dagger.Module;
import dagger.Provides;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.FragmentScope;
import io.github.plastix.forage.ui.base.FragmentModule;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Dagger module that provides dependencies for {@link CacheListFragment} and {@link CacheListActivity}.
 * This is used to inject the cache list presenter and view into each other.
 */
@Module
public class CacheListModule extends FragmentModule {
    public CacheListModule(Fragment fragment) {
        super(fragment);
    }

    @Provides
    @FragmentScope
    public CacheAdapter provideCacheAdapter(Context context, Realm realm) {
        RealmResults<Cache> caches = realm.where(Cache.class).findAllAsync();
        return new CacheAdapter(context, caches, true);
    }

    @Provides
    @FragmentScope
    public LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }
}
