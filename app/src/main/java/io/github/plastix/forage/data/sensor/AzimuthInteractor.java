package io.github.plastix.forage.data.sensor;

import android.hardware.SensorManager;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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

    /**
     * Returns an Observable that emits the Azimuth of the Android compass.
     *
     * @return Observable
     */
    public Observable<Float> getAzimuthObservable() {
        AzimuthObserver observer = azimuthProvider.get();
        observer.setSensorDelay(SensorManager.SENSOR_DELAY_UI);
        return Observable.create(observer)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
