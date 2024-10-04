package com.tutorial.springboot.securityoauth2server.enums;

import java.util.Arrays;
import java.util.List;

public enum GrantType {
    AUTHORIZATION_CODE("code"),
    IMPLICIT("token"),
    PASSWORD("none"),
    CLIENT_CREDENTIALS("none"),
    REFRESH_TOKEN("none"),
    ;

    private final String responseType;

    GrantType(String responseType) {
        this.responseType = responseType;
    }

    public static List<String> toList() {
        return Arrays.stream(GrantType.values()).map(Enum::name).toList();
    }
}
