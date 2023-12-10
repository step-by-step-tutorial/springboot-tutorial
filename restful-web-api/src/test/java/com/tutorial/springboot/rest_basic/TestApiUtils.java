package com.tutorial.springboot.rest_basic;

import org.springframework.web.util.UriComponentsBuilder;

public final class TestApiUtils {

	public TestApiUtils() {
	}

	public static UriComponentsBuilder createTestUriBuilder(int port) {
		return UriComponentsBuilder.newInstance()
				.encode()
				.scheme("http")
				.host("localhost")
				.port(port);
	}

}
