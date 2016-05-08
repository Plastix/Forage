package io.github.plastix.forage.data.api;

import java.util.HashMap;
import java.util.Map;

import io.github.plastix.forage.BuildConfig;

public class AuthManager {

    private static String CONSUMER_KEY = "consumer_key";

    public static Map<String, String> authenticateLevel1() {
        Map<String, String> map = new HashMap<>();
        map.put(CONSUMER_KEY, BuildConfig.OKAPI_US_CONSUMER_KEY);
        return map;
    }

}
