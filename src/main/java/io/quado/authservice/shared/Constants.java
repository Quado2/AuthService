package io.quado.authservice.shared;

import java.math.BigInteger;

public class Constants {

    public static final String LOGIN_URL = "/api/login";
    public  static final String JWT_SECRET = System.getenv("JWT_SECRET");
    public static final String TOKEN_REFRESH_URL =  "/api/token/refresh";
    public static final String ACCES_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final long ACCESS_TOKEN_EXPIRY = 1800000L; // 30 MINS
    public static final long REFRESH_TOKEN_EXPIRY = 2592000000L; // 30 DAYS

}
