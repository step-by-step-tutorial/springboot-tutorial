package com.tutorial.springboot.restful_web_api.util;

import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.tutorial.springboot.restful_web_api.util.HttpUtils.uriOf;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Http Utilities Unit Tests")

public class HttpUtilsTest {

    @BeforeEach
    public void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void givenValidId_thenCreatUri() {
        var givenId = 5L;

        var actual = uriOf(givenId);

        assertNotNull(actual);
        assertEquals("/5", actual.getPath());
    }

    @Test
    public void givenNullId_thenThrowNullPointerException() {
        final Long givenId = null;

        var actual = assertThrows(ValidationException.class, () -> uriOf(givenId));

        assertNotNull(actual);
        assertNotNull(actual.getDetails());
    }

    @Test
    public void givenInValidId_thenThrowNullPointerException() {
        var givenId = -5L;

        var actual = assertThrows(ValidationException.class, () -> uriOf(givenId));

        assertNotNull(actual);
        assertNotNull(actual.getDetails());
    }
}