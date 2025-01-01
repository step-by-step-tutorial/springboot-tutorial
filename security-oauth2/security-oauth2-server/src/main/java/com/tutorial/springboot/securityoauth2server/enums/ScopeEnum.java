package com.tutorial.springboot.securityoauth2server.enums;

import java.util.Arrays;
import java.util.List;

public enum ScopeEnum {
    write,
    read,
    update,
    delete,
    ;

    public static List<String> allType() {
        return Arrays.stream(ScopeEnum.values()).map(Enum::name).toList();
    }
}
