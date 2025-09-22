package com.tutorial.springboot.profile;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.profile.ProfileName.DEV;
import static com.tutorial.springboot.profile.ProfileName.TEST;
import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest {

    @Nested
    @SpringBootTest
    class DefaultProfileLoader {
        @Autowired(required = false)
        private ProfileService activeProfile;

        @Autowired
        private UnProfiledService unProfiled;

        @Test
        void shouldOnlyLoadBeansWithoutProfile() {
            assertNull(activeProfile);

            assertNotNull(unProfiled);
            assertTrue(unProfiled.getProfile().isBlank());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(TEST)
    class TestProfileLoader {
        @Autowired
        private ProfileService activeProfile;

        @Autowired
        private UnProfiledService unProfiled;

        @Test
        void shouldLoadBeanBasedOnTestProfileAndUnProfiled() {
            assertNotNull(activeProfile);
            assertEquals(TEST, activeProfile.getProfile());

            assertNotNull(unProfiled);
            assertTrue(unProfiled.getProfile().isBlank());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(DEV)
    class DevProfileLoader {
        @Autowired
        private ProfileService activeProfile;

        @Autowired
        private UnProfiledService unProfiled;

        @Test
        void shouldLoadBeanBasedOnDevProfileAndUnProfiled() {
            assertNotNull(activeProfile);
            assertEquals(DEV, activeProfile.getProfile());

            assertNotNull(unProfiled);
            assertTrue(unProfiled.getProfile().isBlank());
        }
    }
}