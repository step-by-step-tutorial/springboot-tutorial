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
        private ProfileService beanUnderActiveProfile;

        @Autowired
        private DefaultProfileService beanWithoutProfile;

        @Test
        void shouldOnlyLoadBeansWithoutProfile() {
            assertNull(beanUnderActiveProfile);

            assertNotNull(beanWithoutProfile);
            assertTrue(beanWithoutProfile.getProfile().isBlank());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(TEST)
    @DisplayName("when it uses test profile")
    class TestProfileLoader {
        @Autowired
        private ProfileService beanUnderActiveProfile;

        @Autowired
        private DefaultProfileService beanWithoutProfile;

        @Test
        void shouldLoadBeanUnderTestProfileAndWithoutProfile() {
            assertNotNull(beanUnderActiveProfile);
            assertEquals(TEST, beanUnderActiveProfile.getProfile());

            assertNotNull(beanWithoutProfile);
            assertTrue(beanWithoutProfile.getProfile().isBlank());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(DEV)
    @DisplayName("when it uses dev profile")
    class DevProfileLoader {
        @Autowired
        private ProfileService beanUnderActiveProfile;

        @Autowired
        private DefaultProfileService beanWithoutProfile;

        @Test
        void shouldLoadBeanUnderDevProfileAndWithoutProfile() {
            assertNotNull(beanUnderActiveProfile);
            assertEquals(DEV, beanUnderActiveProfile.getProfile());
            assertNotNull(beanWithoutProfile);
            assertTrue(beanWithoutProfile.getProfile().isBlank());
        }
    }
}