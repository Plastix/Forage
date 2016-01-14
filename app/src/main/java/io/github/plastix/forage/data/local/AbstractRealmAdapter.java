package io.github.plastix.forage.data.local;

import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A RecyclerView adapter for use with Realm
 * Based on from http://stackoverflow.com/questions/28995380/best-practices-to-use-realm-with-a-recycler-view
 */
public abstract class AbstractRealmAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected RealmResults<T> data;
    protected RealmQuery<T> query;
    protected Realm realm;

    public AbstractRealmAdapter(Realm realm) {
        this.realm = realm;
        this.query = getQuery();
        loadData();
    }

    protected abstract RealmQuery<T> getQuery();

//    Probably not a good idea to pass realm queries into the adapter
//    public void setQuery(RealmQuery<T> query) {
//        this.query = query;
//    }

    public void loadData() {
        this.data = query.findAll();
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
        return data.size();
    }

    public void destroy() {
        this.realm.close();
    }
}