package io.github.plastix.forage.data.api;

/**
 * OkApi URL routes and other constants
 * See http://www.opencaching.us/okapi/introduction.html
 */
public class ApiConstants {

    public static final String BASE_ENDPOINT = "http://www.opencaching.us";
    public static final String REQUEST_TOKEN_ENDPOINT = BASE_ENDPOINT + "/okapi/services/oauth/request_token";
    public static final String ACCESS_TOKEN_ENDPOINT = BASE_ENDPOINT + "/okapi/services/oauth/access_token";
    public static final String AUTHORIZATION_WEBSITE_URL = BASE_ENDPOINT + "/okapi/services/oauth/authorize";
    public static final String OAUTH_CALLBACK = "app://forage";
    public static final String OAUTH_ENABLE_HEADER = "OAuth";

    public static final String ENDPOINT_GEOCACHES = "services/caches/geocaches";
    public static final String ENDPOINT_NEAREST = "services/caches/search/nearest";
}
