package io.github.plastix.forage.ui.cachelist;

import android.location.Location;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.ui.base.rx.RxPresenter;
import io.github.plastix.forage.util.RxUtils;
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

    public void getGeocachesFromInternet() {
        // Cancel any currently running request
        RxUtils.safeUnsubscribe(networkSubscription);

        networkInteractor.hasInternetConnectionCompletable().subscribe(
                () -> locationInteractor.isLocationAvailable()
                        .doOnSubscribe(subscription -> setRefreshing())
                        .subscribe(this::fetchGeocaches, throwable -> {
                            if (isViewAttached()) {
                                view.onErrorLocation();
                            }
                        }),
                throwable -> {
                    if (isViewAttached()) {
                        view.onErrorInternet();
                    }
                });

    }

    private void fetchGeocaches() {
        networkSubscription = locationInteractor.getUpdatedLocation()
                .toObservable()
                .compose(CacheListPresenter.this.<Location>deliverFirst())
                .toSingle()
                .flatMap(location -> apiInteractor.getNearbyCaches(location.getLatitude(),
                        location.getLongitude(),
                        NEARBY_CACHE_RADIUS_MILES))
                .doOnSubscribe(this::setRefreshing)
                .subscribe(caches -> {
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

    @Override
    public void onViewAttached(CacheListView view) {
        super.onViewAttached(view);

        // If we have an active networkSubscription it means we are still fetching geocaches
        // from the internet so set the view to refreshing
        if (networkSubscription != null && !networkSubscription.isUnsubscribed()) {
            setRefreshing();
        }
    }

    private void setRefreshing() {
        if (isViewAttached()) {
            view.setRefreshing();
        }
    }

    @Override
    public void onDestroyed() {
        databaseInteractor.onDestroy();
    }
}
