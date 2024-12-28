package com.tutorial.springboot.security_rbac_jwt.fixture.permission;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;

import java.util.List;
import java.util.stream.IntStream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuditAssertionUtils.assertAuditFields;
import static org.junit.jupiter.api.Assertions.*;

public final class PermissionEntityAssertionUtils {

    private PermissionEntityAssertionUtils() {
    }

    public static void assertPermission(Permission actual, long expectedId, int expectedVersion) {
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedVersion, actual.getVersion().intValue());
        assertFalse(actual.getName().isBlank());
        assertFalse(actual.getDescription().isBlank());
        assertAuditFields(actual);
    }

    public static void assertPermissions(List<Permission> actual, int expectedSize, long[] expectedIdentities, int[] expectedVersions) {
        assertNotNull(actual);
        assertEquals(expectedSize, actual.size());
        IntStream.range(0, expectedSize).forEach(index -> assertPermission(actual.get(index), expectedIdentities[index], expectedVersions[index]));
    }
}
