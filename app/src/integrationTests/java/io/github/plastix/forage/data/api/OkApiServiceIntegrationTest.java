package io.github.plastix.forage.data.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import io.github.plastix.forage.ApplicationComponent;
import io.github.plastix.forage.ForageRoboelectricIntegrationTestRunner;
import io.github.plastix.forage.data.local.model.Cache;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.adapter.rxjava.HttpException;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assert_;

@RunWith(ForageRoboelectricIntegrationTestRunner.class)
public class OkApiServiceIntegrationTest {

    private ApplicationComponent applicationComponent = ForageRoboelectricIntegrationTestRunner.
            forageApplication().getComponent();
    private MockWebServer mockWebServer;
    private OkApiService okApiService;

    @Before
    public void beforeEachTest() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        okApiService = applicationComponent.okApiService();

        HostSelectionInterceptor interceptor = applicationComponent.hostInteceptor();
        interceptor.setHost(mockWebServer.url("").toString());
    }

    @After
    public void afterEachTest() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void searchAndRetrieve_shouldHandleCorrectResponse() {
        String jsonResponse = "{\n" +
                "  \"CACHE1\":{\n" +
                "    \"code\":\"CACHE1\",\n" +
                "    \"name\":\"Cache Name 1\",\n" +
                "    \"location\":\"45|45\",\n" +
                "    \"type\":\"Traditional\",\n" +
                "    \"status\":\"Available\",\n" +
                "    \"terrain\":1,\n" +
                "    \"difficulty\":2.5,\n" +
                "    \"size2\":\"micro\",\n" +
                "    \"description\":\"<p>Cache Description 1<\\/p>\"\n" +
                "  },\n" +
                "  \"CACHE2\":{\n" +
                "    \"code\":\"CACHE2\",\n" +
                "    \"name\":\"Cache Name 2\",\n" +
                "    \"location\":\"0|0\",\n" +
                "    \"type\":\"Virtual\",\n" +
                "    \"status\":\"Available\",\n" +
                "    \"terrain\":1,\n" +
                "    \"difficulty\":1,\n" +
                "    \"size2\":\"none\",\n" +
                "    \"description\":\"<p>Cache Description 2<\\/p>\"\n" +
                "  }\n" +
                "}";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));

        // Input fake data to the API call
        List<Cache> caches = okApiService.searchAndRetrieve("", "", "", "", false, "").toBlocking().value();

        assertThat(caches).hasSize(2);

        Cache cache1 = caches.get(0);
        assertThat(cache1.cacheCode).isEqualTo("CACHE1");
        assertThat(cache1.name).isEqualTo("Cache Name 1");
        assertThat(cache1.location.latitude).isWithin(0).of(45);
        assertThat(cache1.location.longitude).isWithin(0).of(45);
        assertThat(cache1.type).isEqualTo("Traditional");
        assertThat(cache1.status).isEqualTo("Available");
        assertThat(cache1.terrain).isWithin(0).of(1);
        assertThat(cache1.difficulty).isWithin(0).of(2.5f);
        assertThat(cache1.size).isEqualTo("Micro");
        assertThat(cache1.description).isEqualTo("Cache Description 1");

        Cache cache2 = caches.get(1);
        assertThat(cache2.cacheCode).isEqualTo("CACHE2");
        assertThat(cache2.name).isEqualTo("Cache Name 2");
        assertThat(cache2.location.latitude).isWithin(0).of(0);
        assertThat(cache2.location.longitude).isWithin(0).of(0);
        assertThat(cache2.type).isEqualTo("Virtual");
        assertThat(cache2.status).isEqualTo("Available");
        assertThat(cache2.terrain).isWithin(0).of(1);
        assertThat(cache2.difficulty).isWithin(0).of(1);
        assertThat(cache2.size).isEqualTo("None");
        assertThat(cache2.description).isEqualTo("Cache Description 2");

    }

    @Test
    public void searchAndRetrieve_shouldThrowExceptionOnWebserverError() {
        for (Integer errorCode : HttpCodes.clientAndServerSideErrorCodes()) {
            mockWebServer.enqueue(new MockResponse().setStatus("HTTP/1.1 " + errorCode + " Nope!"));

            try {
                // Input fake data to the API call
                okApiService.searchAndRetrieve("", "", "", "", false, "").toBlocking().value();
                assert_().fail("HttpException should be thrown for error code: " + errorCode);
            } catch (RuntimeException expected) {
                HttpException httpException = (HttpException) expected.getCause();
                assertThat(httpException.code()).isEqualTo(errorCode);
                assertThat(httpException.message()).isEqualTo("Nope!");
            }
        }
    }

    @Test
    public void searchAndRetrieve_shouldHandleEmptyJson() {
        mockWebServer.enqueue(new MockResponse().setBody("{}"));
        List<Cache> caches = okApiService.searchAndRetrieve("", "", "", "", false, "").toBlocking().value();

        assertThat(caches).isEmpty();
    }

    @Test
    public void searchAndRetrieve_shouldThrowErrorOnInvalidJson() {
        String jsonResponse = "{\n" +
                "  \"CACHE1\":{\n" +
                "    \"code\":\"CACHE1\",\n" +
                "    \"name\":\"Cache Name 1\",\n" +
                "    \"location\":\"45|45\",\n" +
                "    \"type\":\"Traditional\",\n" +
                "    \"status\":\"Available\",\n" +
                "    \"terrain\":1,\n" +
                "    \"difficulty\":2.5,\n" +
                "    \"size2\":\"micro\",\n" +
                "    \"description\":\"<p>Cache Description 1<\\/p>\"\n" +
                "  ";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse));
        try {
            okApiService.searchAndRetrieve("", "", "", "", false, "").toBlocking().value();
            assert_().fail("Should throw error!");
        } catch (Exception ignored) {
            // Pass
        }
    }
}
