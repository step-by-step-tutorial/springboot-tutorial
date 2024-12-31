package com.tutorial.springboot.securityoauth2server.fixture.role;

import com.tutorial.springboot.securityoauth2server.entity.Role;

import java.util.List;
import java.util.stream.IntStream;

import static com.tutorial.springboot.securityoauth2server.testutils.TestAuditAssertionUtils.assertAuditFields;
import static org.junit.jupiter.api.Assertions.*;

public final class RoleEntityAssertionUtils {

    private RoleEntityAssertionUtils() {
    }

    public static void assertRole(Role actual, long expectedId, int expectedVersion) {
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedVersion, actual.getVersion().intValue());
        assertFalse(actual.getName().isBlank());
        assertFalse(actual.getDescription().isBlank());
        assertAuditFields(actual);
    }

    public static void assertRoles(List<Role> actual, int expectedSize, long[] expectedIdentities, int[] expectedVersions) {
        assertNotNull(actual);
        assertEquals(expectedSize, actual.size());
        IntStream.range(0, expectedSize).forEach(index -> assertRole(actual.get(index), expectedIdentities[index], expectedVersions[index]));
    }

}
