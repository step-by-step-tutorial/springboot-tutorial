package com.tutorial.springboot.h2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
public class H2ControllerTest {

  public final Logger logger = LoggerFactory.getLogger(DomainRepositoryTest.class.getSimpleName());

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @BeforeEach
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Test
  public void indexView() throws Exception {
    ModelAndView modelAndView = mvc.perform(get("/")).andReturn().getModelAndView();
    Assertions.assertEquals("forward:/h2-console", modelAndView.getViewName());

    logger.info(String.format("request(/): %s", modelAndView.getViewName()));

  }

}
