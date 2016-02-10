package io.github.plastix.forage.data.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


/**
 * Observable Action for emitting compass orientation from the phone.
 * Sensor math from http://stackoverflow.com/questions/8315913/how-to-get-direction-in-android-such-as-north-west
 */
public class AzimuthProvider implements Observable.OnSubscribe<Float>, SensorEventListener {

    private Observer<? super Float> observer;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] accelermoter_data = null;
    private float[] magnetometer_data = null;

    public AzimuthProvider(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    @Override
    public void call(Subscriber<? super Float> subscriber) {
        this.observer = subscriber;
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        registerSensors();

        subscriber.add(buildUnsubscriber());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // onSensorChanged gets called for each sensor so we have to remember the values
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelermoter_data = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometer_data = event.values.clone();
        }

        if (accelermoter_data != null && magnetometer_data != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, accelermoter_data, magnetometer_data);

            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                float azimuth = (float) (180 * orientation[0] / Math.PI);

                observer.onNext(azimuth);
            } else {
                observer.onError(new Throwable("Error getting sensor data!"));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    private void registerSensors() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    private Subscription buildUnsubscriber() {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                sensorManager.unregisterListener(AzimuthProvider.this, accelerometer);
                sensorManager.unregisterListener(AzimuthProvider.this, magnetometer);
            }
        });
    }
}
