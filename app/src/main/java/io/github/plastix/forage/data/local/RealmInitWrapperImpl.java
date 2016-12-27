package io.github.plastix.forage.data.local;

import android.app.Application;

import io.realm.Realm;

public class RealmInitWrapperImpl implements RealmInitWrapper {

    private Application application;

    public RealmInitWrapperImpl(Application application) {
        this.application = application;
    }

    @Override
    public void init() {
        Realm.init(application);
    }
}
