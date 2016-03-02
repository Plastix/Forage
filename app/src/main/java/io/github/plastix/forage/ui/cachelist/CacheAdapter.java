package io.github.plastix.forage.ui.cachelist;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.AbstractRealmAdapter;
import io.github.plastix.forage.data.local.Cache;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * RecyclerView adapter to get {@link Cache}s from Realm and display them.
 */
public class CacheAdapter extends AbstractRealmAdapter<Cache, CacheAdapter.CacheHolder> {

    @Inject
    public CacheAdapter(Provider<Realm> realm) {
        super(realm);
    }

    @Override
    public CacheHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cacheView = inflater.inflate(R.layout.cache_item, parent, false);

        return new CacheHolder(cacheView);
    }

    @Override
    public void onBindViewHolder(final CacheHolder holder, int position) {
        Cache cache = getItem(position);
        Resources resources = holder.itemView.getContext().getResources();

        holder.cacheName.setText(cache.getName());

        holder.cacheType.setText(resources.getString(R.string.cacheitem_type, cache.getType()));

        holder.cacheTerrain.setText(resources.getString(R.string.cacheitem_terrain, cache.getTerrain()));

        holder.cacheDifficulty.setText(resources.getString(R.string.cacheitem_difficulty, cache.getDifficulty()));

        holder.cacheSize.setText(resources.getString(R.string.cache_item_size, cache.getSize()));

        holder.itemView.setOnClickListener(onClickListener);
    }


    /**
     * Query to get data from Realm.
     *
     * @return RealmQuery to get Realm data with.
     */
    @Override
    protected RealmQuery<Cache> getQuery() {
        return this.realm.get().where(Cache.class);
    }

    public class CacheHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cache_name)
        TextView cacheName;

        @Bind(R.id.cache_terrain)
        TextView cacheTerrain;

        @Bind(R.id.cache_difficulty)
        TextView cacheDifficulty;

        @Bind(R.id.cache_size)
        TextView cacheSize;

        @Bind(R.id.cache_type)
        TextView cacheType;

        public CacheHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
