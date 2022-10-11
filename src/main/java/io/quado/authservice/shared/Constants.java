package io.quado.authservice.shared;

public class Constants {

    public static final String LOGIN_URL = "/api/login";
    public  static final String JWT_SECRET = System.getenv("JWT_SECRET");
    public static final String TOKEN_REFRESH_URL =  "/api/token/refresh";
}
