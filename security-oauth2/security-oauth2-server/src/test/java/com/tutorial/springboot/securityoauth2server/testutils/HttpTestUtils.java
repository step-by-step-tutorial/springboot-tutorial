package com.tutorial.springboot.securityoauth2server.testutils;

public final class HttpTestUtils {

    public static final String TEST_HOSTNAME = "localhost";

    public static final int TEST_PORT = 8080;

    public static final String TEST_USERNAME = "test";

    public static final String TEST_PASSWORD = "test";

    private HttpTestUtils() {
    }

    public static String extractAuthorizationCodeFromUrl(String url) {
        if (url != null && url.contains("code=")) {
            return url.split("code=")[1].split("&")[0];
        }
        return null;
    }
}
