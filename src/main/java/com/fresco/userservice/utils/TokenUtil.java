package com.fresco.userservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class TokenUtil {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("YourSecretKeyForJWTGeneration123!".getBytes());
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public static String generateToken(String subject, String role, boolean verified) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .claim("verified", verified)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
