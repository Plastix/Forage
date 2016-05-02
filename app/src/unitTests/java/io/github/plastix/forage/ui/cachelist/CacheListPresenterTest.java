package io.github.plastix.forage.ui.cachelist;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.plastix.forage.data.ObservableManager;
import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.network.NetworkInteractor;
import rx.Completable;
import rx.Observable;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CacheListPresenterTest {

    private CacheListPresenter cacheListPresenter;
    private CacheListView view;

    private OkApiInteractor okApiInteractor;
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private NetworkInteractor networkInteractor;
    private ObservableManager observableManager;

    @Before
    public void beforeEachTest() {
        okApiInteractor = mock(OkApiInteractor.class);
        databaseInteractor = mock(DatabaseInteractor.class);
        locationInteractor = mock(LocationInteractor.class);
        networkInteractor = mock(NetworkInteractor.class);
        observableManager = mock(ObservableManager.class);

        when(observableManager.isStored(anyString())).thenReturn(false);

        cacheListPresenter = new CacheListPresenter(okApiInteractor,
                databaseInteractor,
                locationInteractor,
                networkInteractor);
        cacheListPresenter.setCache(observableManager);
        view = mock(CacheListView.class);

        cacheListPresenter.setView(view);

        when(locationInteractor.getUpdatedLocation()).thenReturn(Observable.just(mock(Location.class)));
    }

    @Test
    public void fetchGeocaches_savesDownloadedGeocachesToDatabase() {
        List<Cache> caches = asList(mock(Cache.class), mock(Cache.class), mock(Cache.class));
        when(okApiInteractor.getNearbyCaches(anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Observable.just(caches));

        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());
        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());


        cacheListPresenter.onResume();
        cacheListPresenter.fetchGeocaches();

        verify(databaseInteractor).clearAndSaveGeocaches(caches);
    }

    @Test
    public void fetchGeocaches_networkErrorUpdatesView() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(
                Completable.error(new Throwable()));

        cacheListPresenter.onResume();
        cacheListPresenter.fetchGeocaches();

        verify(view).onErrorInternet();
    }

    @Test
    public void fetchGeocaches_locationErrorUpdatesView() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(
                Completable.complete());
        when(locationInteractor.isLocationAvailable()).thenReturn(
                Completable.error(new Throwable()));

        cacheListPresenter.onResume();
        cacheListPresenter.fetchGeocaches();

        verify(view).onErrorLocation();
    }

    @Test
    public void fetchGeocaches_fetchErrorUpdatesView() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(
                Completable.complete());
        when(locationInteractor.isLocationAvailable()).thenReturn(
                Completable.complete());
        when(okApiInteractor.getNearbyCaches(anyDouble(), anyDouble(), anyDouble())).thenReturn(
                Observable.<List<Cache>>error(new Throwable()));

        cacheListPresenter.onResume();
        cacheListPresenter.fetchGeocaches();

        verify(view).onErrorLocation();
        verify(view).onErrorLocation();
    }

    @Test
    public void onDestroy_callsDatabaseInteractor() {
        cacheListPresenter.onDestroy();

        verify(databaseInteractor).onDestroy();
        verifyNoMoreInteractions(databaseInteractor);
    }
}
