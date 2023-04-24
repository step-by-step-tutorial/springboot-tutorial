package com.tutorial.springboot.profile;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.profile.ProfileConstant.DEV;
import static com.tutorial.springboot.profile.ProfileConstant.TEST;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Active Profile Tests")
class ProfileServiceTest {

    @Nested
    @SpringBootTest
    @DisplayName("when it does not use profile")
    class DefaultProfileLoader {
        @Autowired(required = false)
        private ProfileService profileService;

        @Autowired
        private DefaultProfileBean bean;

        @Test
        void GivenDefaultAsProfile_WhenLoadContext_ThenInjectDefaultProfileService() {
            assertNull(profileService);
            assertNotNull(bean);
            assertEquals("", bean.getProfile());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(TEST)
    @DisplayName("when it use test profile")
    class TestProfileLoader {
        @Autowired
        private ProfileService profileService;

        @Autowired
        private DefaultProfileBean bean;

        @Test
        void GivenTestAsProfile_WhenLoadContext_ThenInjectTestProfileService() {
            assertNotNull(profileService);
            assertEquals(TEST, profileService.getProfile());
            assertNotNull(bean);
            assertEquals("", bean.getProfile());
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles(DEV)
    @DisplayName("when it use dev profile")
    class DevProfileLoader {
        @Autowired
        private ProfileService profileService;

        @Autowired
        private DefaultProfileBean bean;

        @Test
        void GivenDevAsProfile_WhenLoadContext_ThenInjectDevProfileService() {
            assertNotNull(profileService);
            assertEquals(DEV, profileService.getProfile());
            assertNotNull(bean);
            assertEquals("", bean.getProfile());
        }
    }
}