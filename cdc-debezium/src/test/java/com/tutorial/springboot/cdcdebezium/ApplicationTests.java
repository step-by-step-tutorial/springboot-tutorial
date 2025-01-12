package com.tutorial.springboot.cdcdebezium;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test", "h2", "kafka-embedded"})
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
