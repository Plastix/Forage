package io.github.plastix.forage.data.local;

import android.app.Application;

import io.realm.Realm;

public class RealmProxyImpl implements RealmProxy {

    private Application application;

    public RealmProxyImpl(Application application) {
        this.application = application;
    }

    @Override
    public void init() {
        Realm.init(application);
    }
}
