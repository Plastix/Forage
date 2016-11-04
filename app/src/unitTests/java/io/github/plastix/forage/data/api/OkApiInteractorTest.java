package io.github.plastix.forage.data.api;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import dagger.Lazy;
import io.github.plastix.forage.data.api.response.SubmitLogResponse;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.rx1.RxSchedulerRule;
import rx.Single;
import rx.singles.BlockingSingle;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OkApiInteractorTest {

    @Rule
    public final RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    @SuppressWarnings("NullableProblems") // Initialized in @Before.
    @NonNull
    private OkApiInteractor okApiInteractor;

    @SuppressWarnings("NullableProblems")
    @NonNull
    private OkApiService okApiService;

    // Input to the interactor.
    // We're mocking the API response so it doesn't matter what we pass into the interceptor
    private double lat, lon, radius;

    @Before
    public void beforeEachTest() {
        okApiService = mock(OkApiService.class);
        Lazy<OkApiService> okApiServiceLazy = () -> okApiService;

        okApiInteractor = new OkApiInteractor(okApiServiceLazy);
        lat = lon = radius = 0;
    }

    @Test
    public void getNearbyItems_shouldReturnItemsFromApi() {
        List<Cache> caches = asList(mock(Cache.class), mock(Cache.class));
        when(
                okApiService.searchAndRetrieve(anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyBoolean(),
                        anyString())
        ).thenReturn(Single.just(caches));

        BlockingSingle<List<Cache>> result = okApiInteractor.getNearbyCaches(lat, lon, radius).toBlocking();
        assertThat(result.value()).containsExactlyElementsIn(caches);
    }

    @Test
    public void getNearbyItems_shouldReturnErrorFromApi() {
        Exception error = new RuntimeException();
        when(
                okApiService.searchAndRetrieve(anyString(),
                        anyString(),
                        anyString(),
                        anyString(),
                        anyBoolean(),
                        anyString())
        ).thenReturn(Single.<List<Cache>>error(error));

        try {
            okApiInteractor.getNearbyCaches(lat, lon, radius).toBlocking().value();
            assert_().fail("Should have throw an error!", error);
        } catch (Exception e) {
            assertThat(e).isSameAs(error);
        }

    }

    @Test
    public void submitLog_shouldReturnResponseFromApi() {
        SubmitLogResponse response = mock(SubmitLogResponse.class);
        when(
                okApiService.submitLog(anyString(), anyString(), anyString())
        ).thenReturn(Single.just(response));

        BlockingSingle<SubmitLogResponse> result = okApiInteractor.submitLog("", "", "").toBlocking();

        assertThat(result.value()).isSameAs(response);
    }

    @Test
    public void submitLog_shouldReturnErrorFromApi() {
        Exception error = new RuntimeException();
        when(
                okApiService.submitLog(anyString(), anyString(), anyString())
        ).thenReturn(Single.error(error));

        try {
            okApiInteractor.submitLog("", "", "").toBlocking().value();
            assert_().fail("Should have throw an error!", error);
        } catch (Exception e) {
            assertThat(e).isSameAs(error);
        }
    }
}
