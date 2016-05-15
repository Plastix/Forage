package io.github.plastix.forage.ui.cachelist;

import android.location.Location;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.Presenter;
import io.github.plastix.forage.util.RxUtils;
import io.realm.OrderedRealmCollection;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class CacheListPresenter extends Presenter<CacheListView> {

    private static final double NEARBY_CACHE_RADIUS_MILES = 100;

    private OkApiInteractor apiInteractor;
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private NetworkInteractor networkInteractor;

    private Subscription databaseSubscription;
    private Subscription networkSubscription = Subscriptions.unsubscribed();


    @Inject
    public CacheListPresenter(OkApiInteractor apiInteractor,
                              DatabaseInteractor databaseInteractor,
                              LocationInteractor locationInteractor,
                              NetworkInteractor networkInteractor) {
        this.apiInteractor = apiInteractor;
        this.databaseInteractor = databaseInteractor;
        this.locationInteractor = locationInteractor;
        this.networkInteractor = networkInteractor;
    }

    public void getGeocachesFromDatabase() {
        databaseSubscription = databaseInteractor.getGeocaches()
                .subscribe(new SingleSubscriber<OrderedRealmCollection<Cache>>() {
                    @Override
                    public void onSuccess(OrderedRealmCollection<Cache> value) {
                        if (isViewAttached()) {
                            view.setGeocacheList(value);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.e("CacheListPresenter", error.getMessage(), error);
                    }
                });
    }


    public void getGeocachesFromInternet() {
        // Cancel any currently running request
        RxUtils.safeUnsubscribe(networkSubscription);

        networkInteractor.hasInternetConnectionCompletable()
                .subscribe(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onErrorInternet();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        locationInteractor.isLocationAvailable()
                                .subscribe(new Action1<Throwable>() {
                                               @Override
                                               public void call(Throwable throwable) {
                                                   view.onErrorLocation();
                                               }
                                           }, new Action0() {
                                               @Override
                                               public void call() {
                                                   networkSubscription = locationInteractor.getUpdatedLocation().flatMap(new Func1<Location, Single<List<Cache>>>() {
                                                       @Override
                                                       public Single<List<Cache>> call(Location location) {
                                                           return apiInteractor.getNearbyCaches(location.getLatitude(), location.getLongitude(), NEARBY_CACHE_RADIUS_MILES);
                                                       }
                                                   }).subscribe(new Action1<List<Cache>>() {
                                                       @Override
                                                       public void call(List<Cache> caches) {
                                                           // The adapter will update automatically after this database write
                                                           databaseInteractor.clearAndSaveGeocaches(caches);
                                                           RxUtils.safeUnsubscribe(networkSubscription);
                                                       }
                                                   }, new Action1<Throwable>() {
                                                       @Override
                                                       public void call(Throwable throwable) {
                                                           if (isViewAttached()) {
                                                               view.onErrorFetch();
                                                           }
                                                           Log.e("CacheListPresenter", throwable.getMessage(), throwable);
                                                       }
                                                   });
                                               }
                                           }
                                );
                    }
                });

    }

    @Override
    public void onViewAttached(CacheListView view) {
        super.onViewAttached(view);

        // If we have an active networkSubscription it means we are still fetching geocaches
        // from the internet so set the view to refreshing
        if (!networkSubscription.isUnsubscribed()) {
            view.setRefreshing();
        }
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();
        RxUtils.safeUnsubscribe(databaseSubscription);
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
        RxUtils.safeUnsubscribe(networkSubscription);
        networkSubscription = null;
        databaseSubscription = null;
    }

    public void clearCaches() {
        databaseInteractor.clearGeocaches();
    }

}
