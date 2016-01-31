package io.github.plastix.forage.data.local;

import org.json.JSONArray;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
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
     * Creates or updates all {@link Cache} objects in the Realm database using the JSON array.
     * Geocaches are updated if they have the same primary key.
     *
     * @param data JSON Array of Geocaches.
     */
    public void saveGeocachesFromJson(final JSONArray data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Cache.class, data);
            }
        }, null);
    }

    /**
     * Removes all caches from the database then updates the database with {@link Cache}s from the
     * JSON array.
     *
     * @param data JSON Array of Geocaches.
     */
    public void clearAndSaveGeocaches(final JSONArray data) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Cache.class).findAll().clear();
                realm.createOrUpdateAllFromJson(Cache.class, data);
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
        return realm.where(Cache.class).contains("code", cacheCode).findFirstAsync()
                // Must filter by loaded objects because findFirstAsync returns a "Future"
                .<Cache>asObservable().filter(new Func1<Cache, Boolean>() {
                    @Override
                    public Boolean call(Cache cache) {
                        return cache.isLoaded();
                    }
                })
                .take(1).toSingle()
                .observeOn(AndroidSchedulers.mainThread());

    }

}
