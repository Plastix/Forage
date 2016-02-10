package io.github.plastix.forage.data.sensor;

import android.hardware.SensorManager;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import javax.inject.Inject;

import rx.Observable;


/**
 * A Reactive wrapper around the Android compass sensor.
 */
public class AzimuthInteractor {

    private SensorManager sensorManager;

    @Inject
    public AzimuthInteractor(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    @RxLogObservable
    public Observable<Float> getAzimuthObservable() {
        return Observable.create(new AzimuthProvider(sensorManager));
    }
}
