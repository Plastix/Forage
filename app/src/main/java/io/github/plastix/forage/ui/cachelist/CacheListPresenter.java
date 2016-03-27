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
import io.github.plastix.forage.ui.ReactivePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class CacheListPresenter extends ReactivePresenter<CacheListView, List<Cache>> {

    private static final String REQUEST_ID = "CacheListPresenter.network";
    private static final double NEARBY_CACHE_RADIUS_MILES = 100;

    private OkApiInteractor apiInteractor;
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private NetworkInteractor networkInteractor;


    @Inject
    public CacheListPresenter(OkApiInteractor apiInteractor,
                              DatabaseInteractor databaseInteractor,
                              LocationInteractor locationInteractor,
                              NetworkInteractor networkInteractor) {
        super();
        this.apiInteractor = apiInteractor;
        this.databaseInteractor = databaseInteractor;
        this.locationInteractor = locationInteractor;
        this.networkInteractor = networkInteractor;
    }


    public void fetchGeocaches() {
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
                                                   // Run the ReactivePresenter observable
                                                   subscribe();
                                               }
                                           }
                                );
                    }
                });

    }

    @Override
    protected void onAttachObservable() {
        super.onAttachObservable();
        view.setRefreshing();
    }


    protected Observable<List<Cache>> buildObservable() {
        return locationInteractor.getUpdatedLocation().flatMap(new Func1<Location, Observable<List<Cache>>>() {
            @Override
            public Observable<List<Cache>> call(Location location) {
                return apiInteractor.getNearbyCaches(location.getLatitude(), location.getLongitude(), NEARBY_CACHE_RADIUS_MILES);
            }
        });
    }


    protected Subscriber<List<Cache>> buildSubscription() {
        return new Subscriber<List<Cache>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.getMessage(), e);
                view.onErrorFetch();
            }

            @Override
            public void onNext(List<Cache> caches) {
                databaseInteractor.clearAndSaveGeocaches(caches);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseInteractor.onDestroy();
    }

    public void clearCaches() {
        databaseInteractor.clearGeocaches();
    }

    protected String getObservableId() {
        return REQUEST_ID;
    }


}
