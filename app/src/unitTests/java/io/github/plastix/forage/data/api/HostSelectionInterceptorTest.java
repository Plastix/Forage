package io.github.plastix.forage.data.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;

public class HostSelectionInterceptorTest {

    private MockWebServer mockWebServer;
    private HostSelectionInterceptor interceptor;
    private OkHttpClient okHttpClient;
    private String hostURL;

    @Before
    public void beforeEachTest() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody("Hello!"));
        mockWebServer.start();
        hostURL = mockWebServer.url("").toString();
        interceptor = new HostSelectionInterceptor();
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @After
    public void afterEachTest() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void responseShouldBeIntercepted() throws IOException {
        String interceptURL = mockWebServer.url("/intercept/").toString();
        interceptor.setHost(interceptURL);

        Request request = new Request.Builder()
                .url(hostURL)
                .build();
        Call call = okHttpClient.newCall(request);

        // Execute the call synchronously
        Response response = call.execute();
        assertThat(response.request().url().toString()).isEqualTo(interceptURL);
    }

    @Test
    public void responseShouldNotBeIntercepted() throws IOException {
        interceptor.setHost(null);

        Request request = new Request.Builder()
                .url(hostURL)
                .build();
        Call call = okHttpClient.newCall(request);

        // Execute the call synchronously
        Response response = call.execute();
        assertThat(response.request().url().toString()).isEqualTo(hostURL);
    }

}
