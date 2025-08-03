package com.example.demo.security;

import com.example.demo.dto.UserDTO;
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
    public String generateJwtToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDTO.getId());
        claims.put("name", userDTO.getName());
        claims.put("email", userDTO.getEmail());
        claims.put("createdDate", userDTO.getCreatedDate().toString());
        claims.put("updatedDate", userDTO.getUpdatedDate().toString());
        claims.put("deleted", userDTO.isDeleted());
                return Jwts
                .builder()
                // Custom Claims
                .setClaims(claims)
                // Registered claims
                .setSubject(String.valueOf(userDTO.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(getKey(), SignatureAlgorithm.HS256)  // signature part
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
    // get user from token claims
    public UserDTO extractUser(String token) {
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return new UserDTO(claims);
        }catch (Exception e){
            System.out.println(e.getMessage());
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