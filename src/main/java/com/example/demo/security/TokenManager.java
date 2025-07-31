package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {

    public static final long TOKEN_VALIDITY = 10 * 60 * 60; // in seconds

    @Value("${secret}")
    private String jwtSecret;

    // returns generated token on successful authentication by the user
    public String generateJwtToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        return Jwts
                .builder()
                // Custom Claims
                .setClaims(claims)
                // Registered claims
                .setSubject(String.valueOf(userDetails.getUsername()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)  // signature part
                .compact();
    }

    // Generate token with user ID
    public String generateJwtToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validates the token
    // Token is parsed for the claims such as username, roles, authorities, validity period etc.
    public boolean validateToken(String token) {
        try {
            final Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token).getBody();
            boolean isTokenExpired = claims.getExpiration().before(new Date());
            return !isTokenExpired;
        } catch (Exception e) {
            return false;
        }
    }

    // Validates the token with user ID check
    public boolean validateToken(String token, int userID) {
        try {
            final int id = extractUserId(token);
            if (id == 0) return false;
            final Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token).getBody();
            boolean isTokenExpired = claims.getExpiration().before(new Date());
            return (id == userID) && !isTokenExpired;
        } catch (Exception e) {
            return false;
        }
    }

    // get user id from sub claim
    public Integer extractUserId(String token) {
        try {
            final Claims claims = Jwts
                    .parserBuilder() // start building parser
                    .setSigningKey(getKey()) // provide with signing key for verification
                    .build() // build parser
                    .parseClaimsJws(token) // verify token and parse
                    .getBody(); // get claims from body
            return Integer.parseInt(claims.getSubject()); // get id from subject claim
        } catch (Exception e) {
            return null; // return null for invalid tokens
        }
    }

    // get email from token
    public String getEmailFromToken(String token) throws NumberFormatException {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email").toString();
        } catch (Exception e) {
            return null;
        }
    }

    // create a signing key based on secret
    private Key getKey() {
        // decode base 64 string to a byte array
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        // generate secret key
        return Keys.hmacShaKeyFor(keyBytes);
    }
}