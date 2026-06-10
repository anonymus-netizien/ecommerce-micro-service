package com.stschool.ecommerce.userservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.stschool.ecommerce.userservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {

    private static final String ROLE_TAG = "role";
    private static final String ISSUED_DATE_TAG = "issued-date";
    private static final String TOKEN_ISSUER = "st-school";

    @Value("${jwt.validity.accessToken}")
    private long ACCESS_TOKEN_VALIDITY_DURATION;
    @Value("${jwt.validity.refreshToken}")
    private long REFRESH_TOKEN_VALIDITY_DURATION;
    @Value("${jwt.secret}")
    private String SECRET;

    public String generateAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_VALIDITY_DURATION)))
                .withIssuer(TOKEN_ISSUER)
                .withClaim(ISSUED_DATE_TAG, new Date())
                .withClaim(ROLE_TAG, List.of(user.getRole().name()))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_VALIDITY_DURATION)))
                .withIssuer(TOKEN_ISSUER)
                .withClaim(ISSUED_DATE_TAG, new Date())
                .withClaim(ROLE_TAG, List.of(user.getRole().name()))
                .sign(algorithm);
    }

}
