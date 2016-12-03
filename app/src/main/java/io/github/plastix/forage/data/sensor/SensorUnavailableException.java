package io.github.plastix.forage.data.sensor;

/**
 * Exception to indicate that an Android sensor is not available
 */
public class SensorUnavailableException extends Throwable {

    public SensorUnavailableException() {
    }

    public SensorUnavailableException(String message) {
        super(message);
    }
}
