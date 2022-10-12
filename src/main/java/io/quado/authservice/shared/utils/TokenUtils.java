package io.quado.authservice.shared.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.quado.authservice.shared.Constants;

import java.util.Date;
import java.util.List;

public class TokenUtils {

    public static String generateAccessToken(String username, String issuer, List<String> roles){
        String secret = Constants.JWT_SECRET;
        return  JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_EXPIRY ))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public static String generateRefreshToken(String username, String requestUrl) {
        String secret = Constants.JWT_SECRET;
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Constants.REFRESH_TOKEN_EXPIRY))
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC256(secret.getBytes()));
    }

    public static DecodedJWT verifyToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
         return verifier.verify(token);

    }


}
