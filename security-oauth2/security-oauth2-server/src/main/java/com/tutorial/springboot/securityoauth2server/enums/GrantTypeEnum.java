package com.tutorial.springboot.securityoauth2server.enums;

import java.util.Arrays;
import java.util.List;

public enum GrantTypeEnum {
    authorization_code,
    implicit,
    password,
    client_credentials,
    refresh_token,
    jwt_bearer,
    ;

    public static List<String> allType() {
        return Arrays.stream(GrantTypeEnum.values()).map(Enum::name).toList();
    }
}
