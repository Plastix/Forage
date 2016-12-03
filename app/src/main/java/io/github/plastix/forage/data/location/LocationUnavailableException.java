package io.github.plastix.forage.data.location;

/**
 * Exception to indicate that location is not available.
 */
public class LocationUnavailableException extends Throwable {
    public LocationUnavailableException(String message) {
        super(message);
    }

    public LocationUnavailableException() {
    }
}