package com.tutorial.springboot.securityoauth2server.config;

import com.tutorial.springboot.securityoauth2server.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtils.getCurrentUsername());
    }
}