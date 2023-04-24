package com.tutorial.springboot.helloworld;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("hello-world: Load context")
class ApplicationTest {

    @Test
    @DisplayName("print message")
    void GivenMessage_WhenInvokePrintMessage_ThenTheMessageWillBePrintedOnConsole() {
        var givenMessage = "Hello World Test";

        System.out.println(givenMessage);

        assertTrue(true);
    }

}
