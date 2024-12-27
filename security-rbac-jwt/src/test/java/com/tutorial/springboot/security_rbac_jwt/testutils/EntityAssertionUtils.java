package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class EntityAssertionUtils {

    private EntityAssertionUtils() {
    }

    public static void assertAuditFields(AbstractEntity<?,?> actual) {
        assertFalse(actual.getCreatedBy().isBlank());
        assertNotNull(actual.getCreatedAt());
        assertFalse(actual.getUpdatedBy().isBlank());
        assertNotNull(actual.getUpdatedAt());
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
