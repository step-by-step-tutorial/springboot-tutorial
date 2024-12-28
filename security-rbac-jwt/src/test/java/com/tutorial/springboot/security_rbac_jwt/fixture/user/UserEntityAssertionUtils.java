package com.tutorial.springboot.security_rbac_jwt.fixture.user;

import com.tutorial.springboot.security_rbac_jwt.entity.User;

import java.util.List;
import java.util.stream.IntStream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuditAssertionUtils.assertAuditFields;
import static org.junit.jupiter.api.Assertions.*;

public final class UserEntityAssertionUtils {

    private UserEntityAssertionUtils() {
    }

    public static void assertUser(User actual, long expectedId, int expectedVersion) {
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedVersion, actual.getVersion().intValue());
        assertFalse(actual.getUsername().isBlank());
        assertFalse(actual.getPassword().isBlank());
        assertFalse(actual.getEmail().isBlank());
        assertTrue(actual.isEnabled());
        assertAuditFields(actual);
    }

    public static void assertUsers(List<User> actual, int expectedSize, long[] expectedIdentities, int[] expectedVersions) {
        assertNotNull(actual);
        assertEquals(expectedSize, actual.size());
        IntStream.range(0, expectedSize).forEach(index -> assertUser(actual.get(index), expectedIdentities[index], expectedVersions[index]));
    }
}
