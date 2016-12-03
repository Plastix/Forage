package io.github.plastix.forage.data.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import javax.inject.Inject;

import io.github.plastix.forage.util.AngleUtils;
import rx.AsyncEmitter;
import rx.functions.Action1;

public class AzimuthAsyncEmitter implements Action1<AsyncEmitter<Float>> {

    private SensorManager sensorManager;
    private WindowManager windowManager;
    private Sensor compass;
    private float[] orientation;
    private float[] rMat;

    @Inject
    public AzimuthAsyncEmitter(@NonNull SensorManager sensorManager,
                               @NonNull WindowManager windowManager) {
        this.sensorManager = sensorManager;
        this.windowManager = windowManager;
        this.orientation = new float[3];
        this.rMat = new float[9];
        this.compass = null;
    }


    @Override
    public void call(AsyncEmitter<Float> emitter) {
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (compass != null) {
            SensorEventListener sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                        // Calculate the rotation matrix
                        SensorManager.getRotationMatrixFromVector(rMat, event.values);

                        // Compass direction is result[0]
                        float[] result = SensorManager.getOrientation(rMat, orientation);
                        float azimuth = (float) (Math.toDegrees(result[0]));
                        azimuth += AngleUtils.getRotationOffset(windowManager);

                        emitter.onNext(azimuth);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {
                    // No op
                }
            };

            sensorManager.registerListener(sensorEventListener, compass, SensorManager.SENSOR_DELAY_GAME);

            emitter.setCancellation(() -> sensorManager.unregisterListener(sensorEventListener, compass));
        } else {
            emitter.onError(new SensorUnavailableException("Compass Sensor not available!"));
        }
    }
}
