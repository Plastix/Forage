package io.github.plastix.forage.data.local;

import com.google.gson.JsonArray;

import javax.inject.Inject;

import io.realm.Realm;

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
    public void saveGeocachesFromJson(final JsonArray data) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Cache.class, data.toString());
            }
        }, null);
    }

}
