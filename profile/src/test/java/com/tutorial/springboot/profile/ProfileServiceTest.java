package com.tutorial.springboot.profile;

import static com.tutorial.springboot.profile.ProfileEnum.DEV;
import static com.tutorial.springboot.profile.ProfileEnum.TEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("test active profiles")
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
    void givenDefaultAsProfile_WhenLoadContext_ThenInjectDefaultProfileService() {
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
    void givenTestAsProfile_WhenLoadContext_ThenInjectTestProfileService() {
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
    void givenDevAsProfile_WhenLoadContext_ThenInjectDevProfileService() {
      assertNotNull(profileService);
      assertEquals(DEV, profileService.getProfile());
      assertNotNull(bean);
      assertEquals("", bean.getProfile());
    }
  }
}