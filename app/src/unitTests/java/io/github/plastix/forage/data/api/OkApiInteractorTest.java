package io.github.plastix.forage.data.api;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import dagger.Lazy;
import io.github.plastix.forage.data.local.model.Cache;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observables.BlockingObservable;
import rx.schedulers.Schedulers;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OkApiInteractorTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private OkApiInteractor okApiInteractor;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private OkApiService okApiService;

    // Input to the interactor.
    // We're mocking the API response so it doesn't matter what we pass into the interceptor
    private double lat, lon, radius;

    @BeforeClass
    public static void onlyOnce() {
        // Use Schedulers.immediate() instead of AndroidSchedulers.mainThread()
        // We can't mock an Android looper on the JVM!
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Before
    public void beforeEachTest() {
        okApiService = mock(OkApiService.class);
        Lazy<OkApiService> okApiServiceLazy = new Lazy<OkApiService>() {
            @Override
            public OkApiService get() {
                return okApiService;
            }
        };

        okApiInteractor = new OkApiInteractor(okApiServiceLazy);
        lat = lon = radius = 0;
    }

    @Test
    public void getNearbyItems_shouldReturnItemsFromApi() {
        List<Cache> caches = asList(mock(Cache.class), mock(Cache.class));
        when(
                okApiService.searchAndRetrieve(anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyString())
        ).thenReturn(Observable.just(caches));

        BlockingObservable<List<Cache>> result = okApiInteractor.getNearbyCaches(lat, lon, radius).toBlocking();
        assertThat(result.first()).containsExactlyElementsIn(caches);
    }

    @Test
    public void getNearbyItems_shouldReturnErrorFromApi() {
        Exception error = new RuntimeException();
        when(
                okApiService.searchAndRetrieve(anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyString())
        ).thenReturn(Observable.<List<Cache>>error(error));

        try {
            okApiInteractor.getNearbyCaches(lat, lon, radius).toBlocking().first();
            assert_().fail("Should have throw an error!", error);
        } catch (Exception e) {
            assertThat(e).isSameAs(error);
        }

    }

}
