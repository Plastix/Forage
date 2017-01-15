package io.github.plastix.forage.ui.log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.plastix.forage.R;
import io.github.plastix.forage.data.api.OkApiInteractor;
import io.github.plastix.forage.data.api.response.SubmitLogResponse;
import io.github.plastix.forage.data.local.DatabaseInteractor;
import io.github.plastix.forage.data.local.model.Cache;
import io.github.plastix.forage.data.network.NetworkInteractor;
import io.github.plastix.forage.data.network.NetworkUnavailableException;
import io.github.plastix.rx1.RxSchedulerRule;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Completable;
import rx.Single;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class LogPresenterTest {

    @Rule
    public RxSchedulerRule rxSchedulerRule = new RxSchedulerRule();

    private LogPresenter logPresenter;

    @Mock
    private LogView view;

    @Mock
    private OkApiInteractor okApiInteractor;

    @Mock
    private NetworkInteractor networkInteractor;

    @Mock
    private DatabaseInteractor databaseInteractor;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);

        logPresenter = new LogPresenter(okApiInteractor, networkInteractor, databaseInteractor);
        logPresenter.onViewAttached(view);
    }

    @Test
    public void submitLog_NoInternet() {
        when(networkInteractor.hasInternetConnectionCompletable()).
                thenReturn(Completable.error(new NetworkUnavailableException()));

        when(okApiInteractor.submitLog(anyString(), anyString(), anyString()))
                .thenReturn(Single.just(mock(SubmitLogResponse.class)));

        logPresenter.submitLog("", "", "");

        verify(view, times(1)).showErrorInternetDialog();
    }

    @Test
    public void submitLog_showSuccessfulSubmit() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());

        SubmitLogResponse response = mock(SubmitLogResponse.class);
        response.isSuccessful = true;
        Single<SubmitLogResponse> single = Single.just(response);
        when(okApiInteractor.submitLog(anyString(), anyString(), anyString()))
                .thenReturn(single);

        logPresenter.submitLog("", "", "");

        verify(view, times(1)).showSubmittingDialog();
        verify(view, times(1)).showSuccessfulSubmit();
    }

    @Test
    public void submitLog_showFailedSubmit() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());

        SubmitLogResponse response = mock(SubmitLogResponse.class);
        response.isSuccessful = false;
        response.message = "Message";
        Single<SubmitLogResponse> single = Single.just(response);
        when(okApiInteractor.submitLog(anyString(), anyString(), anyString()))
                .thenReturn(single);

        logPresenter.submitLog("", "", "");

        verify(view, times(1)).showSubmittingDialog();
        verify(view, times(1)).showErrorDialog(response.message);
    }

    @Test
    public void submitLog_onHttpError() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());

        HttpException exception = new HttpException(Response.error(404, mock(ResponseBody.class)));
        Single<SubmitLogResponse> single = Single.error(exception);
        when(okApiInteractor.submitLog(anyString(), anyString(), anyString()))
                .thenReturn(single);

        logPresenter.submitLog("", "", "");

        verify(view, times(1)).showSubmittingDialog();
        verify(view, times(1)).showErrorDialog(exception.getMessage());
    }

    @Test
    public void submitLog_onHttpErrorUnknown() {
        when(networkInteractor.hasInternetConnectionCompletable()).thenReturn(Completable.complete());

        Single<SubmitLogResponse> single = Single.error(new Throwable("Error!"));
        when(okApiInteractor.submitLog(anyString(), anyString(), anyString()))
                .thenReturn(single);

        logPresenter.submitLog("", "", "");

        verify(view, times(1)).showSubmittingDialog();
        verify(view, times(1)).showErrorDialog(R.string.log_submit_error_unknown);
    }

    @Test
    public void getLogTypes_setEventCacheTypes() {
        Cache cache = mock(Cache.class);
        cache.type = "Event";

        Single<Cache> singleResult = Single.just(cache);
        when(databaseInteractor.getGeocache(anyString())).thenReturn(singleResult);

        String cacheCode = "";
        logPresenter.getLogTypes(cacheCode);

        verify(view).setLogTypes(R.array.log_types_event);
    }

    @Test
    public void getLogTypes_doNotUpdateTypes() {
        Cache cache = mock(Cache.class);
        cache.type = "Traditional";
        Single<Cache> singleResult = Single.just(cache);
        when(databaseInteractor.getGeocache(anyString())).thenReturn(singleResult);

        String cacheCode = "";
        logPresenter.getLogTypes(cacheCode);

        verifyZeroInteractions(view);
    }

    @Test
    public void getLogTypes_onErrorDoesNothing() {
        Single<Cache> error = Single.error(new Throwable("Error!"));
        when(databaseInteractor.getGeocache(anyString())).thenReturn(error);

        String cacheCode = "";
        logPresenter.getLogTypes(cacheCode);

        verifyZeroInteractions(view);
    }

    @Test
    public void onDestroy_shouldCallDatabaseInteractor() {
        logPresenter.onDestroyed();

        verify(databaseInteractor, times(1)).onDestroy();
        verifyNoMoreInteractions(databaseInteractor);
    }
}
