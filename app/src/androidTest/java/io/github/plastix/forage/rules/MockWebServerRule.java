package io.github.plastix.forage.rules;

import android.support.annotation.NonNull;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;

import io.github.plastix.forage.data.api.HostSelectionInterceptor;
import io.github.plastix.forage.util.TestUtils;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Adapted from https://github.com/artem-zinnatullin/qualitymatters
 */
public class MockWebServerRule implements TestRule {

    @NonNull
    private final Object testClassInstance;

    @NonNull
    private HostSelectionInterceptor interceptor;

    public MockWebServerRule(@NonNull Object testClassInstance) {
        this.testClassInstance = testClassInstance;
        interceptor = TestUtils.app().getComponent().hostInteceptor();
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                final NeedsMockWebServer needsMockWebServer = description.getAnnotation(NeedsMockWebServer.class);

                if (needsMockWebServer != null) {
                    final MockWebServer mockWebServer = new MockWebServer();
                    mockWebServer.start();

                    interceptor.setHost(mockWebServer.url("").toString());

                    if (!needsMockWebServer.setupMethod().isEmpty()) {
                        final Method setupMethod = testClassInstance.getClass().getDeclaredMethod(needsMockWebServer.setupMethod(), MockWebServer.class);
                        setupMethod.invoke(testClassInstance, mockWebServer);
                    }

                    // Try to evaluate the test and anyway shutdown the MockWebServer.
                    try {
                        base.evaluate();
                    } finally {
                        mockWebServer.shutdown();
                    }
                } else {
                    // No need to setup a MockWebServer, just evaluate the test.
                    base.evaluate();
                }
            }
        };
    }
}
