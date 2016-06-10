package io.github.plastix.forage.ui.map;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.location.LocationInteractor;
import io.realm.OrderedRealmCollection;
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
    private DatabaseInteractor databaseInteractor;
    private LocationInteractor locationInteractor;
    private MapActivityView view;

    @Before
    public void beforeEachTest() {
        view = mock(MapActivityView.class);
        databaseInteractor = mock(DatabaseInteractor.class);
        locationInteractor = mock(LocationInteractor.class);
        mapPresenter = new MapPresenter(databaseInteractor, locationInteractor);
        mapPresenter.onViewAttached(view);
    }

    @Test
    public void onDestroy_shouldCallDatabaseInteractorDestroy() {
        mapPresenter.onDestroyed();
        verify(databaseInteractor, only()).onDestroy();
    }

    @Test
    public void getGeocaches_shouldCallViewPopulateMap() {
        //noinspection unchecked
        OrderedRealmCollection<Cache> caches = mock(OrderedRealmCollection.class);
        when(databaseInteractor.getGeocaches()).thenReturn(Single.just(caches));

        Location location = mock(Location.class);
        when(locationInteractor.getUpdatedLocation()).thenReturn(Single.just(location));

        mapPresenter.setupMap();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, times(1)).addMapMarkers(caches);

        verify(locationInteractor, only()).getUpdatedLocation();
        verify(view, times(1)).animateMapCamera(location);
    }

    @Test
    public void getGeocaches_errorDoesNothing() {
        when(databaseInteractor.getGeocaches()).
                thenReturn(Single.<OrderedRealmCollection<Cache>>error(new Throwable("Error")));
        when(locationInteractor.getUpdatedLocation()).thenReturn(Single.error(new Throwable("Error 2")));

        mapPresenter.setupMap();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, never()).addMapMarkers(anyListOf(Cache.class));

        verify(locationInteractor, only()).getUpdatedLocation();
        verify(view, never()).animateMapCamera(any(Location.class));
    }
}
