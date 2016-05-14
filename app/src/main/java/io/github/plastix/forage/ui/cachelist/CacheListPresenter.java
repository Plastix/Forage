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
import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscriber;
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
    private Subscription subscription = Subscriptions.unsubscribed();


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
        databaseInteractor.getGeocaches().subscribe(new SingleSubscriber<OrderedRealmCollection<Cache>>() {
            @Override
            public void onSuccess(OrderedRealmCollection<Cache> value) {
                view.setGeocacheList(value);
            }

            @Override
            public void onError(Throwable error) {
                Log.e("CacheListPresenter", error.getMessage(), error);
            }
        });
    }


    public void getGeocachesFromInternet() {
        // Cancel any currently running request
        unsubscribe();

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
                                                   subscription = locationInteractor.getUpdatedLocation().flatMap(new Func1<Location, Observable<List<Cache>>>() {
                                                       @Override
                                                       public Observable<List<Cache>> call(Location location) {
                                                           return apiInteractor.getNearbyCaches(location.getLatitude(), location.getLongitude(), NEARBY_CACHE_RADIUS_MILES);
                                                       }
                                                   }).subscribe(new Subscriber<List<Cache>>() {
                                                       @Override
                                                       public void onCompleted() {

                                                       }

                                                       @Override
                                                       public void onError(Throwable e) {
                                                           Log.e("CacheListPresenter", e.getMessage(), e);
                                                           view.onErrorFetch();
                                                       }

                                                       @Override
                                                       public void onNext(List<Cache> caches) {
                                                           databaseInteractor.clearAndSaveGeocaches(caches);
                                                           subscription.unsubscribe();
                                                       }
                                                   });
                                               }
                                           }
                                );
                    }
                });

    }

    public void unsubscribe() {
        RxUtils.safeUnsubscribe(subscription);
    }

    @Override
    public void onViewAttached(CacheListView view) {
        super.onViewAttached(view);

        if (!subscription.isUnsubscribed()) {
            view.setRefreshing();
        }
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }

    public void clearCaches() {
        databaseInteractor.clearGeocaches();
    }

}
