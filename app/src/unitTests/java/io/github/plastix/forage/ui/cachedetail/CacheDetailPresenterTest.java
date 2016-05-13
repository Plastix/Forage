package io.github.plastix.forage.ui.cachedetail;

import org.junit.Before;
import org.junit.Test;

import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import rx.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CacheDetailPresenterTest {

    private CacheDetailPresenter cacheDetailPresenter;
    private DatabaseInteractor databaseInteractor;
    private CacheDetailView view;

    @Before
    public void beforeEachTest() {
        view = mock(CacheDetailView.class);
        databaseInteractor = mock(DatabaseInteractor.class);
        cacheDetailPresenter = new CacheDetailPresenter(databaseInteractor);
        cacheDetailPresenter.onViewAttached(view);
    }

    @Test
    public void onDestroy_shouldCallDatabaseInteractorDestroy() {
        cacheDetailPresenter.onDestroyed();
        verify(databaseInteractor, only()).onDestroy();
    }

    @Test
    public void getGeocache_shouldCallViewReturnedGeocache() {
        Cache cache = mock(Cache.class);
        String cacheCode = "geocache code";
        when(databaseInteractor.getGeocache(cacheCode)).thenReturn(Single.just(cache));

        cacheDetailPresenter.getGeocache(cacheCode);

        verify(databaseInteractor, only()).getGeocache(cacheCode);
        verify(view, only()).returnedGeocache(cache);
    }

    @Test
    public void getGeocache_shouldCallViewError() {
        String cacheCode = "geocache code";
        when(databaseInteractor.getGeocache(cacheCode))
                .thenReturn(Single.<Cache>error(new Throwable("Error")));

        cacheDetailPresenter.getGeocache(cacheCode);

        verify(databaseInteractor, only()).getGeocache(cacheCode);
        verify(view, only()).onError();
    }
}
