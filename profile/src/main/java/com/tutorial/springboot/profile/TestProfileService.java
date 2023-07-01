package com.tutorial.springboot.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(ProfileName.TEST)
public class TestProfileService implements ProfileService {

    @Override
    public String getProfile() {
        return ProfileName.TEST;
    }
}
