package com.tutorial.springboot.securityoauth2server.util;

import java.util.Date;

import static java.lang.System.currentTimeMillis;

public final class DateUtils {

    private DateUtils() {
    }

    public static Date currentDate() {
        return new Date(currentTimeMillis());
    }

    public static Date getDateIn(int hours) {
        return new Date(currentTimeMillis() + 1000L * 60 * 60 * hours);
    }

}
