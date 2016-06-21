package io.github.plastix.forage.data.api;

/**
 * OkApi URL routes and other constants
 * See http://www.opencaching.us/okapi/introduction.html
 */
public class ApiConstants {

    public static final String BASE_ENDPOINT = "http://www.opencaching.us/okapi/";
    public static final String REQUEST_TOKEN_ENDPOINT = BASE_ENDPOINT + "services/oauth/request_token";
    public static final String ACCESS_TOKEN_ENDPOINT = BASE_ENDPOINT + "services/oauth/access_token";
    public static final String AUTHORIZATION_WEBSITE_URL = BASE_ENDPOINT + "services/oauth/authorize";
    public static final String ENDPOINT_GEOCACHES = "services/caches/geocaches";
    public static final String ENDPOINT_NEAREST = "services/caches/search/nearest";

    public static final String OAUTH_CALLBACK = "app://forage";
    public static final String OAUTH_ENABLE_HEADER = "OAuth";
}
