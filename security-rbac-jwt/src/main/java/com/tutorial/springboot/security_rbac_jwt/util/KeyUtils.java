package com.tutorial.springboot.security_rbac_jwt.util;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public final class KeyUtils {

    public static final SecretKey SIGN_IN_KEY = Jwts.SIG.HS256.key().build();

}
