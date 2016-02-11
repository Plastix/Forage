package io.github.plastix.forage.data.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


/**
 * Observable Action for emitting compass orientation from the phone.
 */
public class AzimuthObserver implements Observable.OnSubscribe<Float>, SensorEventListener {

    private Observer<? super Float> observer;
    private SensorManager sensorManager;
    private Sensor compass;
    private float[] orientation;
    private float[] rMat;

    @Inject
    public AzimuthObserver(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        this.orientation = new float[3];
        this.rMat = new float[9];
    }

    @Override
    public void call(Subscriber<? super Float> subscriber) {
        this.observer = subscriber;
        this.compass = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        registerSensors();

    subscriber.add(buildUnsubscriber());
}

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // Calculate the rotation matrix
            SensorManager.getRotationMatrixFromVector(rMat, event.values);

            // Compass direction is result[0]
            float[] result = SensorManager.getOrientation(rMat, orientation);
            float azimuth = (float) ((Math.toDegrees(result[0]) + 360) % 360);

            observer.onNext(azimuth);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    private void registerSensors() {
        sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME);
    }

    private Subscription buildUnsubscriber() {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                sensorManager.unregisterListener(AzimuthObserver.this, compass);
            }
        });
    }
}
