package io.github.plastix.forage.data.network;

public class NetworkUnavailableException extends Throwable {
    public NetworkUnavailableException(String message) {
        super(message);
    }

    public NetworkUnavailableException() {
    }
}
