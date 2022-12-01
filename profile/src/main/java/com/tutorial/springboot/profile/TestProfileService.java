package com.tutorial.springboot.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ProfileConstant.TEST)
public class TestProfileService implements ProfileService {

  @Override
  public String getProfile() {
    return ProfileConstant.TEST;
  }
}
