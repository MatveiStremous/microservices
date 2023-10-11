package com.example.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_access_secret}")
    private String accessSecret;
    @Value("${token_lifetime_in_minutes}")
    private Integer TOKEN_LIFETIME_IN_MINUTES;
    private final String SUBJECT = "User details";
    private final String ISSUER = "Matthew";

    public String generateAccessToken(String login) {
        Date expirationDate = Date.from(ZonedDateTime
                .now()
                .plusMinutes(TOKEN_LIFETIME_IN_MINUTES)
                .toInstant());

        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim("login", login)
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(accessSecret));
    }
}
