package com.tutorial.springboot.helloworld;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("load context")
class ApplicationTest {

    @Test
    @DisplayName("print a message")
    void GivenMessage_WhenInvokePrintMessage_ThenTheMessageWillBePrintedOnConsole() {
        var givenMessage = "Hello World Test";

        assertDoesNotThrow(() -> System.out.println(givenMessage));
    }

}
