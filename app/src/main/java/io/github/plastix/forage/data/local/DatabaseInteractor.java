package io.github.plastix.forage.data.local;

import com.google.gson.JsonArray;

import javax.inject.Inject;

import io.realm.Realm;

public class DatabaseInteractor {

    private Realm realm;

    @Inject
    public DatabaseInteractor(Realm realm) {
        this.realm = realm;
    }

    public void clearGeocaches() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Cache.class).findAll().clear();
            }
        }, null); // Passing a null callback makes this transaction async which doesn't block other writes
    }

    public void saveGeocachesFromJson(final JsonArray data) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Cache.class, data.toString());
            }
        }, null);
    }

}
