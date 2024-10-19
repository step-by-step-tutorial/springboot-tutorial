package com.tutorial.springboot.securityoauth2client.dto;

import java.util.List;
import java.util.Map;

public record OAuth2AuthenticationDto(
        String principal,
        List<String> grantedAuthorities,
        Map<String, Object> userAttributes,
        boolean authenticated,
        List<String> grantedAuthoritiesDetails
) {
}