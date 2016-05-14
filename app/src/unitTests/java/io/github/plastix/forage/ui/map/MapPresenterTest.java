package io.github.plastix.forage.ui.map;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.realm.OrderedRealmCollection;
import rx.Single;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapPresenterTest {

    private MapPresenter mapPresenter;
    private DatabaseInteractor databaseInteractor;
    private MapFragView view;

    @Before
    public void beforeEachTest() {
        view = mock(MapFragView.class);
        databaseInteractor = mock(DatabaseInteractor.class);
        mapPresenter = new MapPresenter(databaseInteractor);
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

        mapPresenter.getGeocaches();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, only()).populateMap(caches);
    }

    @Test
    public void getGeocaches_errorDoesNothing() {
        when(databaseInteractor.getGeocaches()).
                thenReturn(Single.<OrderedRealmCollection<Cache>>error(new Throwable("Error")));

        mapPresenter.getGeocaches();

        verify(databaseInteractor, only()).getGeocaches();
        verify(view, never()).populateMap(anyListOf(Cache.class));
    }
}
