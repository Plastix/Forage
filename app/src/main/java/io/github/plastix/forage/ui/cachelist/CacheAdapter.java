package io.github.plastix.forage.ui.cachelist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.plastix.forage.R;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.ui.cachedetail.CacheDetailActivity;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * RecyclerView adapter to get {@link Cache}s from Realm and display them.
 */
public class CacheAdapter extends RealmRecyclerViewAdapter<Cache, CacheAdapter.CacheHolder> {

    public CacheAdapter(Context context, OrderedRealmCollection<Cache> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
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

        holder.cacheName.setText(cache.name);
        holder.cacheType.setText(resources.getString(R.string.cacheitem_type, cache.type));
        holder.cacheTerrain.setText(String.valueOf(cache.terrain));
        holder.cacheDifficulty.setText(String.valueOf(cache.difficulty));
        holder.cacheSize.setText(cache.size);
    }


    public class CacheHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cache_name)
        TextView cacheName;

        @BindView(R.id.cache_terrain)
        TextView cacheTerrain;

        @BindView(R.id.cache_difficulty)
        TextView cacheDifficulty;

        @BindView(R.id.cache_size)
        TextView cacheSize;

        @BindView(R.id.cache_type)
        TextView cacheType;

        public CacheHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            CacheAdapter adapter = CacheAdapter.this;
            Context context = adapter.context;

            Cache item = getItem(getLayoutPosition());
            if (item != null) {

                Intent intent = CacheDetailActivity.newIntent(context, item.cacheCode);
                context.startActivity(intent);
            }
        }
    }

}
