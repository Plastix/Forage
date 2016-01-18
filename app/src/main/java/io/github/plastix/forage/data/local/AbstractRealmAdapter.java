package io.github.plastix.forage.data.local;

import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A RecyclerView adapter for use with Realm
 * Based on from http://stackoverflow.com/questions/28995380/best-practices-to-use-realm-with-a-recycler-view
 */
public abstract class AbstractRealmAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements RealmChangeListener {

    protected RealmResults<T> data;
    protected RealmQuery<T> query;
    protected Realm realm;

    public AbstractRealmAdapter(Realm realm) {
        this.realm = realm;
        loadData();
    }

    public void loadData() {
        setQuery();
        this.data = query.findAllAsync();
        this.data.addChangeListener(this);
    }

    private void setQuery() {
        this.query = getQuery();
    }

    protected abstract RealmQuery<T> getQuery();

    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public final int getItemCount() {
        if (!data.isLoaded()) {
            return 0;
        } else {
            return data.size();
        }
    }

    public void closeRealm() {
        this.realm.close();
    }
}