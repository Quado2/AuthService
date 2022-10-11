package io.quado.authservice.shared;

public class Constants {

    public static final String LOGIN_URL = "/api/login";
    public  static final String JWT_SECRET = System.getenv("JWT_SECRET");
    public static final String TOKEN_REFRESH_URL =  "/api/token/refresh";
    public static final String ACCES_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";
    public static final Integer ACCES_TOKEN_EXPIRY = 30*60*1000; // 30 MINS
    public static final Integer REFRESH_TOKEN_EXPIRY =  30 * 24* 60 * 60 * 1000; // 30 DAYS

}
