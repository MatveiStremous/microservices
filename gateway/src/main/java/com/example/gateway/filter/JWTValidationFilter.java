package com.example.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JWTValidationFilter extends AbstractGatewayFilterFactory<JWTValidationFilter.Config> {
    public JWTValidationFilter() {
        super(Config.class);
    }

    @Value("${jwt_access_secret}")
    private String accessSecret;

    private final String TOKEN_PREFIX = "Bearer ";
    private final Integer TOKEN_START_POSITION = 7;
    private final String SUBJECT = "User details";
    private final String ISSUER = "Matthew";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith(TOKEN_PREFIX)) {
                String jwt = authHeader.substring(TOKEN_START_POSITION);
                try {
                    validateToken(jwt);
                } catch (JWTVerificationException exc) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }

    public void validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(accessSecret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();
        verifier.verify(token);
    }
}
