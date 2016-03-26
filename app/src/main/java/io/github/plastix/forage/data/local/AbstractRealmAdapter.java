package io.github.plastix.forage.data.local;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import dagger.Lazy;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * A RecyclerView adapter for use with Realm.
 * Based on http://stackoverflow.com/questions/28995380/best-practices-to-use-realm-with-a-recycler-view
 */
public abstract class AbstractRealmAdapter<T extends RealmObject, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements RealmChangeListener {

    protected RealmResults<T> data;
    protected RealmQuery<T> query;
    protected Lazy<Realm> realm;
    protected View.OnClickListener onClickListener;

    public AbstractRealmAdapter(Lazy<Realm> realm) {
        this.realm = realm;
        this.onClickListener = null;
        loadData();
    }

    /**
     * Loads data asynchronously from Realm into the adapter.
     * Data is queried using {@link #getQuery()} from the subtype.
     */
    public void loadData() {
        // The query must be reset every time because Realm's findAllAsync "consumes" the query
        setQuery();
        this.data = query.findAllAsync();
        this.data.addChangeListener(this);
    }

    private void setQuery() {
        this.query = getQuery();
    }

    /**
     * Gets the RealmQuery to load data from.
     *
     * @return RealmQuery to load data from.
     */
    protected abstract RealmQuery<T> getQuery();

    /**
     * Called automatically by Realm when the data updates.
     */
    @Override
    public void onChange() {
        notifyDataSetChanged();
    }

    /**
     * Gets the RealmObject at the specified position in the adapter's RealmResults.
     *
     * @param position Position to query.
     * @return Realm object at the specified location.
     * @throws IndexOutOfBoundsException if {@code position < 0 || position >= getItemCount()}.
     */
    public T getItem(int position) {
        return data.get(position);
    }

    /**
     * Returns whether the adapter currently has any items.
     *
     * @return True if empty, else false.
     */
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    /**
     * Returns the number of items currently in the adapter.
     *
     * @return Number of items.
     */
    @Override
    public final int getItemCount() {
        // Realm async queries return a "Future" so we have to check if our data is loaded first.
        // If our Realm data isn't loaded, tell the client we have no items.
        if (!data.isLoaded()) {
            return 0;
        } else {
            return data.size();
        }
    }

    /**
     * Closes the realm instance held by the adapter.
     */
    public void closeRealm() {
        this.realm.get().close();
    }

    /**
     * Sets the click listeners for items in the RecyclerView.
     *
     * @param onClickListener Click listener to set.
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}