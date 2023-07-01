package com.tutorial.springboot.messaging_rabbit_mq;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import com.tutorial.springboot.messaging_rabbit_mq.service.MainQueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles({"test"})
class ApplicationTests {

	@Autowired
	private MainQueueService systemUnderTest;

	@Test
	void contextLoads() {
		systemUnderTest.sendMessage(new MessageModel(UUID.randomUUID().toString(), "test text"));
	}

}
