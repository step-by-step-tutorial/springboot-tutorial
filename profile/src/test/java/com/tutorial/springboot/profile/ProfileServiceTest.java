package com.tutorial.springboot.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.profile.ProfileName.DEV;
import static com.tutorial.springboot.profile.ProfileName.TEST;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("unit tests of active profile")
class ProfileServiceTest {

    @Nested
    @SpringBootTest
    @DisplayName("when it does not use profile")
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
    @DisplayName("when it uses test profile")
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
    @DisplayName("when it uses dev profile")
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