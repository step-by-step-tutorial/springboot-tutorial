package com.tutorial.springboot.securityoauth2server.enums;

import java.util.Arrays;
import java.util.List;

public enum GrantType {
    AUTHORIZATION_CODE,
    PASSWORD,
    CLIENT_CREDENTIALS,
    REFRESH_TOKEN,
    ;

    public static List<String> toList() {
        return Arrays.stream(GrantType.values()).map(Enum::name).toList();
    }
}
