package com.tutorial.springboot.helloworld;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

  @Test
  void printMessage() {
    System.out.println("Hello World Test");
    assertTrue(true);
  }

}
