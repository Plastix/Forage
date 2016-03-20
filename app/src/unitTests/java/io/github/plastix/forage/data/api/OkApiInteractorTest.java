package io.github.plastix.forage.data.api;

import android.location.Location;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.github.plastix.forage.data.local.model.Cache;
import rx.Observable;
import rx.observables.BlockingObservable;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OkApiInteractorTest {

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private OkApiInteractor okApiInteractor;

    // Input to the interactor.
    // We're mocking the API response so it doesn't matter what we pass into the interceptor
    private Location location;
    private double radius;

    @Before
    public void beforeEachTest() {
        okApiInteractor = mock(OkApiInteractor.class);
        location = null;
        radius = 0;
    }

    @Test
    public void getNearbyItems_shouldReturnItemsFromApi() {
        List<Cache> caches = asList(mock(Cache.class), mock(Cache.class));
        when(okApiInteractor.getNearbyCaches(location, radius)).thenReturn(Observable.just(caches));

        BlockingObservable<List<Cache>> result = okApiInteractor.getNearbyCaches(location, radius).toBlocking();
        assertThat(result.first()).containsExactlyElementsIn(caches);
    }

    @Test
    public void getNearbyItems_shouldReturnErrorFromApi() {
        Exception error = new RuntimeException();
        when(okApiInteractor.getNearbyCaches(location, radius)).thenReturn(Observable.<List<Cache>>error(error));

        try {
            okApiInteractor.getNearbyCaches(location, radius).toBlocking().first();
            assert_().fail("Should have throw an error!", error);
        } catch (Exception e) {
            assertThat(e).isSameAs(error);
        }

    }

}
