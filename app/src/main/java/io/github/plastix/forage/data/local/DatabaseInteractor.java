package io.github.plastix.forage.data.local;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import io.github.plastix.forage.ui.LifecycleCallbacks;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Single;
import rx.functions.Func1;

/**
 * Wrapper around Realm DB operations.
 */
public class DatabaseInteractor implements LifecycleCallbacks {

    private Lazy<Realm> realm;

    @Inject
    public DatabaseInteractor(Lazy<Realm> realm) {
        this.realm = realm;
    }

    /**
     * Removes all {@link Cache} objects from the Realm database.
     */
    public void clearGeocaches() {
        realm.get().executeTransaction(new Realm.Transaction() {
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
        realm.get().executeTransaction(new Realm.Transaction() {
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
        return realm.get().where(Cache.class).contains("cacheCode", cacheCode).findFirstAsync()
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
        return realm.get().where(Cache.class).findAllAsync()
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

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        this.realm.get().close();
    }
}
