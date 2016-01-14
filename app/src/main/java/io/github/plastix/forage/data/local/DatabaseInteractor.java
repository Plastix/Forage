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

    // TODO Don't expose Realm's transaction callback
    public void saveCachesFromJson(final JsonArray data, final Realm.Transaction.Callback callback) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createOrUpdateAllFromJson(Cache.class, data.toString());
            }
        }, callback);
    }

}
