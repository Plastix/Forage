package io.github.plastix.forage.data.local;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.plastix.forage.ForageRoboelectricIntegrationTestRunner;
import io.github.plastix.forage.data.local.model.Cache;

import static com.google.common.truth.Truth.assertThat;

@RunWith(ForageRoboelectricIntegrationTestRunner.class)
public class CacheTest {

    @Test
    public void fromJson() {

        Gson gson = ForageRoboelectricIntegrationTestRunner.forageApplication().getComponent().gson();

        String input = "{\n" +
                "  \"code\": \"CACHE_CODE\",\n" +
                "  \"name\": \"Cache Name\",\n" +
                "  \"location\": \"40.7095|-74.0116\",\n" +
                "  \"type\": \"Traditional\",\n" +
                "  \"status\": \"Available\",\n" +
                "  \"terrain\": 1,\n" +
                "  \"difficulty\": 2.5,\n" +
                "  \"size2\": \"micro\",\n" +
                "  \"description\": \"<p>HTML Description</p>\"\n" +
                "}";
        Cache cache = gson.fromJson(input, Cache.class);

        assertThat(cache.cacheCode).isEqualTo("CACHE_CODE");
        assertThat(cache.name).isEqualTo("Cache Name");
        assertThat(cache.location.latitude).isWithin(0).of(40.7095);
        assertThat(cache.location.longitude).isWithin(0).of(-74.0116);
        assertThat(cache.type).isEqualTo("Traditional");
        assertThat(cache.status).isEqualTo("Available");
        assertThat(cache.terrain).isWithin(0).of(1);
        assertThat(cache.difficulty).isWithin(0).of(2.5f);
        assertThat(cache.size).isEqualTo("Micro");
        assertThat(cache.description).isEqualTo("HTML Description");
    }
}
