package io.github.plastix.forage.data.sensor;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import rx.Observable;


/**
 * A Reactive wrapper around the Android compass sensor.
 */
@Singleton
public class AzimuthInteractor {

    private Provider<AzimuthObserver> azimuthProvider;

    @Inject
    public AzimuthInteractor(Provider<AzimuthObserver> azimuthProvider) {
        this.azimuthProvider = azimuthProvider;
    }

    public Observable<Float> getAzimuthObservable() {
        return Observable.create(azimuthProvider.get());
    }
}
