package io.github.plastix.forage.ui.map;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.github.plastix.forage.data.location.LocationUnavailableException;
import io.realm.OrderedRealmCollection;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapPresenterTest {

    private MapPresenter mapPresenter;

    @Mock
    private DatabaseInteractor databaseInteractor;

    @Mock
    private LocationInteractor locationInteractor;

    @Mock
    private MapActivityView view;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        mapPresenter = new MapPresenter(databaseInteractor, locationInteractor);
        mapPresenter.onViewAttached(view);
    }

    @Test
    public void onDestroy_shouldCallDatabaseInteractorDestroy() {
        mapPresenter.onDestroyed();
        verify(databaseInteractor, only()).onDestroy();
    }

    @Test
    public void setupMap_shouldCallViewPopulateMap() {
        //noinspection unchecked
        OrderedRealmCollection<Cache> caches = mock(OrderedRealmCollection.class);
        when(databaseInteractor.getGeocaches()).thenReturn(Single.just(caches));

        mapPresenter.setupMap();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, times(1)).addMapMarkers(caches);
    }

    @Test
    public void setupMap_errorDoesNothing() {
        when(databaseInteractor.getGeocaches()).
                thenReturn(Single.<OrderedRealmCollection<Cache>>error(new Throwable("Error")));


        mapPresenter.setupMap();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, never()).addMapMarkers(anyListOf(Cache.class));
    }

    @Test
    public void centerMapOnLocation_shouldCallView() {
        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());
        Location location = mock(Location.class);
        when(locationInteractor.getUpdatedLocation()).thenReturn(Single.just(location));

        mapPresenter.centerMapOnLocation();

        verify(locationInteractor, times(1)).isLocationAvailable();
        verify(locationInteractor, times(1)).getUpdatedLocation();
        verify(view, times(1)).animateMapCamera(location);

    }

    @Test
    public void centerMapOnLocation_errorDoesNothing() {
        when(locationInteractor.isLocationAvailable()).thenReturn(Completable.complete());
        when(locationInteractor.getUpdatedLocation()).thenReturn(Single.error(new Throwable("Error 2")));

        mapPresenter.centerMapOnLocation();

        verify(locationInteractor, times(1)).isLocationAvailable();
        verify(locationInteractor, times(1)).getUpdatedLocation();
        verify(view, never()).animateMapCamera(any(Location.class));

    }

    @Test
    public void centerMapOnLocation_errorOnNoLocationDoesNothing() {
        when(locationInteractor.isLocationAvailable())
                .thenReturn(Completable.error(new LocationUnavailableException()));
        when(locationInteractor.getUpdatedLocation())
                .thenReturn(Single.just(mock(Location.class)));

        mapPresenter.centerMapOnLocation();

        verify(locationInteractor, times(1)).isLocationAvailable();
        verify(locationInteractor, times(1)).getUpdatedLocation();
        verify(view, never()).animateMapCamera(any(Location.class));
    }
}
