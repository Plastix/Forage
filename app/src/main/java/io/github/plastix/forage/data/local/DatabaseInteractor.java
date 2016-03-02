package io.github.plastix.forage.data.local;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Single;
import rx.functions.Func1;

/**
 * Wrapper around Realm DB operations.
 */
public class DatabaseInteractor {

    private Realm realm;

    @Inject
    public DatabaseInteractor(Realm realm) {
        this.realm = realm;
    }

    /**
     * Removes all {@link Cache} objects from the Realm database.
     */
    public void clearGeocaches() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Cache.class).findAll().clear();
            }
        }, null); // Passing a null callback makes this transaction async which doesn't block other writes
    }


    /**
     * Removes all caches from the database then updates the database with {@link Cache}s from the
     * specified list.
     *
     * @param data JSON Array of Geocaches.
     */
    public void clearAndSaveGeocaches(final List<Cache> data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Cache.class).findAll().clear();
                realm.copyToRealmOrUpdate(data);
            }
        }, null);
    }


    /**
     * Returns a rx.Single of the Geocache with the specified OpenCaching cache code. If the geocache
     * does not exist in the database then the Single's onError method is called.
     *
     * @param cacheCode Cache code of the geocache to query.
     * @return rx.Single with the Geocache.
     */
    public Single<Cache> getGeocache(final String cacheCode) {
        return realm.where(Cache.class).contains("cacheCode", cacheCode).findFirstAsync()
                // Must filter by loaded objects because findFirstAsync returns a "Future"
                .<Cache>asObservable().filter(new Func1<Cache, Boolean>() {
                    @Override
                    public Boolean call(Cache cache) {
                        return cache.isLoaded();
                    }
                })
                .take(1).toSingle();

    }

    /**
     * Returns a rx.Single of all the geocaches currently stored locally in the database.
     *
     * @return rx.Single with List of geocaches.
     */
    public Single<List<Cache>> getGeocaches() {
        return realm.where(Cache.class).findAllAsync()
                .<List<Cache>>asObservable()
                .filter(new Func1<RealmResults<Cache>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Cache> caches) {
                        return caches.isLoaded();
                    }
                }).map(new Func1<RealmResults<Cache>, List<Cache>>() {
                    @Override
                    public List<Cache> call(RealmResults<Cache> caches) {
                        return caches;
                    }
                })
                .take(1).toSingle();

    }

}
