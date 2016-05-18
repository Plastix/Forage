package io.github.plastix.forage.data.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.github.plastix.forage.data.local.model.Cache;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import rx.Single;
import rx.functions.Func1;

/**
 * Wrapper around Realm DB operations.
 */
public class DatabaseInteractor {

    private Lazy<Realm> realm;

    @Inject
    public DatabaseInteractor(@NonNull Lazy<Realm> realm) {
        this.realm = realm;
    }

    /**
     * Removes all {@link Cache} objects from the Realm database.
     */
    public void clearGeocaches() {
        realm.get().executeTransactionAsync(realm1 -> realm1.delete(Cache.class));
    }


    /**
     * Removes all caches from the database then updates the database with {@link Cache}s from the
     * specified list.
     *
     * @param data JSON Array of Geocaches.
     */
    public void clearAndSaveGeocaches(@Nullable final List<Cache> data) {
        realm.get().executeTransactionAsync(realm1 -> {
            realm1.delete(Cache.class);
            realm1.copyToRealmOrUpdate(data);
        });
    }

    /**
     * Returns a rx.Single of the Geocache with the specified OpenCaching cache code. This copies the
     * object from the Realm database so it is no longer live!
     * <p/>
     * See {@link #getGeocache(String)} if you want a live object.
     *
     * @param cacheCode Cache code of geocache to query.
     * @return rx.Single with Geocache.
     */
    public Single<Cache> getGeocacheCopy(@NonNull final String cacheCode) {
        return getGeocache(cacheCode)
                .map(cache -> realm.get().copyFromRealm(cache));
    }

    /**
     * Returns a rx.Single of the Geocache with the specified OpenCaching cache code. If the geocache
     * does not exist in the database then the Single's onError method is called.
     *
     * @param cacheCode Cache code of the geocache to query.
     * @return rx.Single with the Geocache.
     */
    public Single<Cache> getGeocache(@NonNull final String cacheCode) {
        return realm.get().where(Cache.class).contains("cacheCode", cacheCode).findFirstAsync()
                // Must filter by loaded objects because findFirstAsync returns a "Future"
                .<Cache>asObservable().filter(cache -> cache.isLoaded())
                .take(1).toSingle();

    }

    /**
     * Returns a rx.Single of all the geocaches currently stored locally in the database.
     *
     * @return rx.Single with List of geocaches.
     */
    public Single<OrderedRealmCollection<Cache>> getGeocaches() {
        return realm.get().where(Cache.class).findAllAsync()
                .<OrderedRealmCollection<Cache>>asObservable()
                .filter(new Func1<OrderedRealmCollection<Cache>, Boolean>() {
                    @Override
                    public Boolean call(OrderedRealmCollection<Cache> caches) {
                        return caches.isLoaded();
                    }
                }).map(new Func1<OrderedRealmCollection<Cache>, OrderedRealmCollection<Cache>>() {
                    @Override
                    public OrderedRealmCollection<Cache> call(OrderedRealmCollection<Cache> caches) {
                        return caches;
                    }
                })
                .take(1).toSingle();

    }

    public void onDestroy() {
        this.realm.get().close();
    }
}
