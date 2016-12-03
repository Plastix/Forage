package io.github.plastix.forage.data.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import rx.Completable;

/**
 * Wrapper around Android network services.
 */
public class NetworkInteractor {

    private ConnectivityManager connectivityManager;

    @Inject
    public NetworkInteractor(@NonNull ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean hasInternetConnection() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @NonNull
    public Completable hasInternetConnectionCompletable() {
        if (hasInternetConnection()) {
            return Completable.complete();
        } else {
            return Completable.error(new NetworkUnavailableException("Network unavailable!"));
        }
    }

}
