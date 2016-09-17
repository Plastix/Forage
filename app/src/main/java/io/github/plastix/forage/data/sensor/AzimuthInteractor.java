package io.github.plastix.forage.data.sensor;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Provider;

import rx.AsyncEmitter;
import rx.Observable;


/**
 * A Reactive wrapper around the Android compass sensor.
 */
public class AzimuthInteractor {

    private Provider<AzimuthAsyncEmitter> azimuthProvider;

    @Inject
    public AzimuthInteractor(@NonNull Provider<AzimuthAsyncEmitter> azimuthProvider) {
        this.azimuthProvider = azimuthProvider;
    }

    /**
     * Returns an Observable that emits the Azimuth of the Android compass.
     *
     * @return Observable
     */
    @NonNull
    public Observable<Float> getAzimuthObservable() {
        AzimuthAsyncEmitter azimuthAsyncEmitter = azimuthProvider.get();
        return Observable.fromEmitter(azimuthAsyncEmitter, AsyncEmitter.BackpressureMode.LATEST);
    }
}
