package com.tutorial.springboot.security_rbac_jwt.util;

import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static Date getFutureDateIn(int hours) {
        return new Date(System.currentTimeMillis() + 1000L * 60 * 60 * hours);
    }

}
