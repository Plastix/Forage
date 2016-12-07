package io.github.plastix.forage.ui.cachelist;

import javax.inject.Inject;

import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.location.LocationUnavailableException;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.data.network.NetworkUnavailableException;
import io.github.plastix.forage.ui.base.RxPresenter;
import io.github.plastix.forage.util.RxUtils;
import io.github.plastix.rxdelay.RxDelay;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class CacheListPresenter extends RxPresenter<CacheListView> {

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

    public void getGeocachesFromInternet() {
        // Cancel any currently running request
        RxUtils.safeUnsubscribe(subscription);

        subscription = networkInteractor.hasInternetConnectionCompletable()
                .andThen(locationInteractor.isLocationAvailable())
                .andThen(locationInteractor.getUpdatedLocation())
                .flatMap(location -> apiInteractor.getNearbyCaches(location.getLatitude(),
                        location.getLongitude(), NEARBY_CACHE_RADIUS_MILES))
                .compose(RxDelay.delaySingle(getViewState()))
                .doOnSubscribe(this::setRefreshing)
                .subscribe(caches -> {
                    // The adapter will update automatically after this database write
                    databaseInteractor.clearAndSaveGeocaches(caches);
                    RxUtils.safeUnsubscribe(subscription);
                }, throwable -> {
                    Timber.e(throwable, "Error fetching caches!");
                    if (isViewAttached()) {
                        if (throwable instanceof NetworkUnavailableException) {
                            view.onErrorInternet();
                        } else if (throwable instanceof LocationUnavailableException) {
                            view.onErrorLocation();
                        } else {
                            view.onErrorFetch();
                        }
                    }
                });

        addSubscription(subscription);
    }

    @Override
    public void onViewAttached(CacheListView view) {
        super.onViewAttached(view);

        // If we have an active network call it means we are still fetching geocaches
        // from the internet so set the view to refreshing
        if (subscription != null && !subscription.isUnsubscribed()) {
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
