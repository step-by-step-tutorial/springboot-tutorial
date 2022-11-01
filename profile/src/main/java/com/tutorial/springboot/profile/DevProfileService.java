package com.tutorial.springboot.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ProfileEnum.DEV)
public class DevProfileService implements ProfileService {

  @Override
  public String getProfile() {
    return ProfileEnum.DEV;
  }
}
