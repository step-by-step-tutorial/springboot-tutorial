package com.tutorial.springboot.messaging_rabbit_mq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private MessageProducer systemUnderTest;

	@Test
	void contextLoads() {
		systemUnderTest.sendMessage("Hello");
	}

}
