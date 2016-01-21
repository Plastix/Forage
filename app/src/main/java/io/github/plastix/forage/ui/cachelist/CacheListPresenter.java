package io.github.plastix.forage.ui.cachelist;

import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONArray;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.ui.LifecycleCallbacks;
import io.github.plastix.forage.util.NetworkUtils;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class CacheListPresenter implements LifecycleCallbacks {

    private CacheListView view;
    private OkApiInteractor apiInteractor;
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private ConnectivityManager connectivityManager;
    private Subscription subscription;


    @Inject
    public CacheListPresenter(CacheListView view, OkApiInteractor apiInteractor,
                              DatabaseInteractor databaseInteractor,
                              LocationInteractor locationInteractor,
                              ConnectivityManager connectivityManager) {
        this.view = view;
        this.apiInteractor = apiInteractor;
        this.databaseInteractor = databaseInteractor;
        this.locationInteractor = locationInteractor;
        this.connectivityManager = connectivityManager;
        this.subscription = Subscriptions.empty();
    }

    public void getCaches() {
        cancelRequest();

        if (!NetworkUtils.hasInternetConnection(connectivityManager)) {
            view.onErrorInternet();
        } else {

            this.subscription = locationInteractor.getUpdatedLocation().flatMap(new Func1<Location, Single<JSONArray>>() {
                @Override
                public Single<JSONArray> call(Location location) {
                    return apiInteractor.getNearbyCaches(location);
                }
            }).subscribe(new SingleSubscriber<JSONArray>() {
                @Override
                public void onSuccess(JSONArray value) {
                    databaseInteractor.saveGeocachesFromJson(value);
                }

                @Override
                public void onError(Throwable error) {
                    Log.e("error", error.getMessage(), error);
                    view.onErrorInternet();
                }
            });

        }
    }

    public void cancelRequest() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void clearCaches() {
        databaseInteractor.clearGeocaches();
    }

    @Override
    public void onStart() {
        locationInteractor.onStart();
    }

    @Override
    public void onStop() {
        locationInteractor.onStop();

    }

    @Override
    public void onResume() {

    }


}
