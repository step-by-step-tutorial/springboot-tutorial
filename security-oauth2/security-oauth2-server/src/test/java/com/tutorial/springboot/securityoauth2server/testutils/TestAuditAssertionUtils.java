package com.tutorial.springboot.securityoauth2server.testutils;

import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;
import com.tutorial.springboot.securityoauth2server.entity.AbstractEntity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class TestAuditAssertionUtils {

    private TestAuditAssertionUtils() {
    }

    public static void assertAuditFields(AbstractDto<?, ?> actual) {
        assertFalse(actual.getCreatedBy().isBlank());
        assertNotNull(actual.getCreatedAt());
        assertFalse(actual.getUpdatedBy().isBlank());
        assertNotNull(actual.getUpdatedAt());
    }

    public static void assertAuditFields(AbstractEntity<?, ?> actual) {
        assertFalse(actual.getCreatedBy().isBlank());
        assertNotNull(actual.getCreatedAt());
        assertFalse(actual.getUpdatedBy().isBlank());
        assertNotNull(actual.getUpdatedAt());
    }

}
