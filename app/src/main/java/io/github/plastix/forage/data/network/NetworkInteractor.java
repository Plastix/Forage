package io.github.plastix.forage.data.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Completable;

/**
 * Wrapper around Android network services.
 */
public class NetworkInteractor {

    private ConnectivityManager connectivityManager;

    @Inject
    public NetworkInteractor(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean hasInternetConnection() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public Completable hasInternetConnectionCompletable() {
        if (hasInternetConnection()) {
            return Completable.complete();
        } else {
            return Completable.error(new Throwable("Network connection not available!"));
        }
    }
}
