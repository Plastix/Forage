package io.github.plastix.forage.rules;

import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * From https://github.com/artem-zinnatullin/qualitymatters
 * @see MockWebServerRule
 */
public @interface NeedsMockWebServer {

    /**
     * Optional specifier for a setupMethod that needs to be invoked during initialization
     * of {@link okhttp3.mockwebserver.MockWebServer}.
     * <p>
     * Useful for setting up responses that you simply can not define in the test code because app already hit the server.
     *
     * @return Empty string if no method invocation required or public method name that needs to be called.
     */
    @NonNull String setupMethod() default "";
}