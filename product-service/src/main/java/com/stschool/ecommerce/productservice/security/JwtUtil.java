package com.stschool.ecommerce.productservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class JwtUtil {

    private static final String ROLE_TAG = "role";

    @Value("${jwt.secret}")
    private String SECRET;

    public String retrieveTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    private DecodedJWT decodedJWT(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes(StandardCharsets.UTF_8));
        return JWT.require(algorithm).build().verify(token);
    }

    public String extractEmailFromToken(String token) {
        return decodedJWT(token).getSubject();
    }

    public List<String> extractRolesFromToken(String token) {
        return decodedJWT(token).getClaim(ROLE_TAG).asList(String.class);
    }

}
