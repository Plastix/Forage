package io.github.plastix.forage.ui.cachelist;

import android.location.Location;
import android.util.Log;

import com.google.gson.JsonArray;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.realm.Realm;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class CacheListPresenter {

    private CacheListView view;
    private OkApiInteractor apiInteractor;
    private DatabaseInteractor databaseInteractor;
    private Subscription subscription;

    @Inject
    public CacheListPresenter(CacheListView view, OkApiInteractor apiInteractor, DatabaseInteractor databaseInteractor) {
        this.view = view;
        this.apiInteractor = apiInteractor;
        this.databaseInteractor = databaseInteractor;
        this.subscription = Subscriptions.empty();
    }

    public void getCaches() {
        //TODO Reactive location with Google Play Services
        Location location = new Location("");
        location.setLongitude(74.0059);
        location.setLatitude(40.7127);
        apiInteractor.getNearbyCaches(location).subscribe(new SingleSubscriber<JsonArray>() {
            @Override
            public void onError(Throwable e) {
                Log.e("error", e.getMessage(), e);
            }

            @Override
            public void onSuccess(final JsonArray value) {
                databaseInteractor.saveCachesFromJson(value, new Realm.Transaction.Callback() {
                    @Override
                    public void onSuccess() {
                        view.updateList();
                    }
                });

            }

        });
    }

    public void cancelRequest() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


}
