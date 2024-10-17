package com.tutorial.springboot.securityoauth2client.enums;

import java.util.Arrays;
import java.util.List;

public enum GrantType {
    AUTHORIZATION_CODE,
    IMPLICIT,
    PASSWORD,
    CLIENT_CREDENTIALS,
    REFRESH_TOKEN,
    JWT_BEARER,
    ;

    public static List<String> allType() {
        return Arrays.stream(GrantType.values()).map(Enum::name).toList();
    }
}
