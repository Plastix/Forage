package io.github.plastix.forage.ui.cachelist;

import android.location.Location;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.github.plastix.forage.util.RxUtils;
import io.realm.OrderedRealmCollection;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class CacheListPresenter extends RxPresenter<CacheListView> {

    private static final double NEARBY_CACHE_RADIUS_MILES = 100;

    private OkApiInteractor apiInteractor;
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private NetworkInteractor networkInteractor;

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
        addSubscription(
                databaseInteractor.getGeocaches()
                        .toObservable()
                        .compose(this.<OrderedRealmCollection<Cache>>deliverFirst())
                        .toSingle()
                        .subscribe(caches -> {
                                    if (isViewAttached()) {
                                        view.setGeocacheList(caches);
                                    }
                                },
                                throwable -> {
                                    // TODO show error dialog
                                    Timber.e(throwable.getMessage(), throwable);
                                })
        );
    }


    public void getGeocachesFromInternet() {
        // Cancel any currently running request
        RxUtils.safeUnsubscribe(networkSubscription);

        networkInteractor.hasInternetConnectionCompletable()
                .subscribe(throwable -> view.onErrorInternet(), () -> locationInteractor.isLocationAvailable()
                        .subscribe(throwable -> view.onErrorLocation(), () -> {
                                    networkSubscription = locationInteractor.getUpdatedLocation()
                                            .toObservable()
                                            .compose(CacheListPresenter.this.<Location>deliverFirst())
                                            .toSingle()
                                            .flatMap(location -> apiInteractor.getNearbyCaches(location.getLatitude(), location.getLongitude(), NEARBY_CACHE_RADIUS_MILES)).subscribe(caches -> {
                                                // The adapter will update automatically after this database write
                                                databaseInteractor.clearAndSaveGeocaches(caches);
                                                RxUtils.safeUnsubscribe(networkSubscription);
                                            }, throwable -> {
                                                if (isViewAttached()) {
                                                    view.onErrorFetch();
                                                }
                                                Timber.e(throwable.getMessage(), throwable);
                                            });

                                    addSubscription(networkSubscription);
                                }
                        ));

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
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
