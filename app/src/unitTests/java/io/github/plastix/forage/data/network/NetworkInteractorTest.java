package io.github.plastix.forage.data.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;

import rx.Completable;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkInteractorTest {

    private NetworkInteractor networkInteractor;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Before
    public void beforeEachTest() {
        networkInfo = mock(NetworkInfo.class);
        connectivityManager = mock(ConnectivityManager.class);
        networkInteractor = new NetworkInteractor(connectivityManager);
    }

    @Test
    public void hasInternetConnection_shouldReturnFalseWhenNoNetwork() {
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(null);

        assertThat(networkInteractor.hasInternetConnection()).isFalse();
    }

    @Test
    public void hasInternetConnection_shouldReturnFalseWhenNotConnected() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        assertThat(networkInteractor.hasInternetConnection()).isFalse();
    }

    @Test
    public void hasInternetConnection_shouldReturnTrueWhenConnected() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        assertThat(networkInteractor.hasInternetConnection()).isTrue();
    }

    @Test
    public void hasInternetConnectionCompletable_shouldCompleteWhenConnected() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        assertThat(networkInteractor.hasInternetConnectionCompletable()).isEqualTo(Completable.complete());
    }

    @Test
    public void hasInternetConnectionCompletable_shouldErrorWhenNotConnected() {
        when(networkInfo.isConnectedOrConnecting()).thenReturn(false);
        when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);

        assertThat(networkInteractor.hasInternetConnectionCompletable().get()).isInstanceOf(Throwable.class);
    }
}
