package com.tutorial.springboot.rest_basic;

import org.springframework.web.util.UriComponentsBuilder;

public final class TestApiUtils {
	private static final String HTTP_SCHEMA = "http";

	private static final String HOST = "localhost";


	public TestApiUtils() {
	}

	public static UriComponentsBuilder createUriBuilder(int port) {
		return UriComponentsBuilder.newInstance()
				.encode()
				.scheme(HTTP_SCHEMA)
				.host(HOST)
				.port(port);
	}

}
