package io.github.plastix.forage.util;

import android.support.annotation.NonNull;

import io.github.plastix.forage.ForageApplication;

public class TestUtils {

    private TestUtils() {
        throw new IllegalStateException("No instantiation!");
    }

    @NonNull
    public static ForageApplication app() {
        return (ForageApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
    }
}
