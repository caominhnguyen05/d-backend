package com.backend.dashboard_tool.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for generating and validating JWT tokens.
 * This class provides methods to create a JWT token with a specified expiration time and secret key.
 */
@Component
public class JwtTokenUtil {
    private static final String JWT_SECRET = "55942d75e369cbbd1d03e9b004b85483ad4b1f753c10889cd37f5b76f02b2af717a3a9edaf78039a2fc52e46e18218ccebdc8d5da9661a66cb44314eb47f28b8";
    private static final long EXPIRATION = 259200000L;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Generates a JWT token with the current date and an expiration date.
     * The token is signed with the specified secret key.
     * 
     * @return the generated JWT token
     */
    public String generateToken() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION);
      
        return Jwts.builder()
                .setSubject("user")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Getter for the secret key.
     * This method returns the secret key used for signing the JWT tokens.
     * @return the secret key
     */
    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
    /**
     * Getter for the JWT secret.
     * This method returns the JWT secret used for signing the tokens.
     * @return the JWT secret
     */
    public static String getJwtSecret() {
        return JWT_SECRET;
    }
    /**
     * Getter for the expiration time.
     * This method returns the expiration time for the JWT tokens in milliseconds.
     * @return the expiration time
     */
    public static long getExpiration() {
        return EXPIRATION;
    }
}