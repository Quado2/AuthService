package io.quado.authservice.shared.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.quado.authservice.shared.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtils {


    private Integer accessTokenDuration = Constants.ACCES_TOKEN_EXPIRY;
    private Integer refreshTokenDuration = Constants.REFRESH_TOKEN_EXPIRY;


    public static String generateAccessToken(String username, String issuer, List<String> roles){
        String secret = System.getenv("JWT_SECRET");
        return  JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.ACCES_TOKEN_EXPIRY))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public static String generateRefreshToken(String username, String requestUrl) {
        String secret = System.getenv("JWT_SECRET");
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.REFRESH_TOKEN_EXPIRY))
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }


}
