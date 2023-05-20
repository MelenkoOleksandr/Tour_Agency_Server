package com.example.touragency.utils;

import com.example.touragency.entities.User;
import io.jsonwebtoken.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TokenCreator {
    public static String createAccessToken(String userId, String userType, String secretKey, int expirationMinutes) {
        /*
         * Create a JWT access token with the given user_id, secret_key, and expiration time.
         */
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expirationMinutes, ChronoUnit.MINUTES);
        return Jwts.builder()
                .setSubject(userId)
                .claim("userType", userType)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static String createRefreshToken(String userId, String userType, String secretKey, int expirationDays) {
        /*
         * Create a JWT refresh token with the given user_id, secret_key, and expiration time.
         */
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expirationDays, ChronoUnit.DAYS);
        return Jwts.builder()
                .setSubject(userId)
                .claim("userType", userType)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static Jws<Claims> validateToken(String token, String secretKey) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

}
