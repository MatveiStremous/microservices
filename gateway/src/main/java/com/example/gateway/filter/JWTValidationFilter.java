package com.example.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JWTValidationFilter extends AbstractGatewayFilterFactory<JWTValidationFilter.Config> {
    public JWTValidationFilter() {
        super(Config.class);
    }

    @Value("${jwt_access_secret}")
    private String accessSecret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
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
                .withSubject("User details")
                .withIssuer("Matthew")
                .build();
        verifier.verify(token);
    }
}
