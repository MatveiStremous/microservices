package com.example.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_access_secret}")
    private String accessSecret;

    public String generateAccessToken(String login) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(30).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("login", login)
                .withIssuedAt(new Date())
                .withIssuer("Matthew")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(accessSecret));
    }
}
