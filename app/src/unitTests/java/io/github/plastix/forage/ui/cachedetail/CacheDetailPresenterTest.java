package io.github.plastix.forage.ui.cachedetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.data.api.auth.OAuthInteractor;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import rx.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CacheDetailPresenterTest {

    private CacheDetailPresenter cacheDetailPresenter;

    @Mock
    private DatabaseInteractor databaseInteractor;

    @Mock
    private OAuthInteractor oAuthInteractor;

    @Mock
    private CacheDetailView view;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        cacheDetailPresenter = new CacheDetailPresenter(databaseInteractor, oAuthInteractor);
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
        when(databaseInteractor.getGeocacheCopy(cacheCode)).thenReturn(Single.just(cache));

        cacheDetailPresenter.getGeocache(cacheCode);

        verify(view, only()).returnedGeocache(cache);
    }

    @Test
    public void getGeocache_shouldCallViewError() {
        String cacheCode = "geocache code";
        when(databaseInteractor.getGeocacheCopy(cacheCode))
                .thenReturn(Single.<Cache>error(new Throwable("Error")));

        cacheDetailPresenter.getGeocache(cacheCode);

        verify(view, only()).onError();
    }

    @Test
    public void openLogScreen_shouldOpenActivity() {
        when(oAuthInteractor.hasSavedOAuthTokens()).thenReturn(true);

        cacheDetailPresenter.openLogScreen();

        verify(view, only()).openLogScreen();
    }

    @Test
    public void openLogScreen_shouldShowError() {
        when(oAuthInteractor.hasSavedOAuthTokens()).thenReturn(false);

        cacheDetailPresenter.openLogScreen();

        verify(view, only()).onErrorRequiresLogin();
    }
}
