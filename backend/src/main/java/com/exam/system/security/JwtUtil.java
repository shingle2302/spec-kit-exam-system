package com.exam.system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.util.function.Function;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Generate a secure key for HS512 algorithm
    private SecretKey getSigningKey() {
        // Check if the secret is already a valid base64 string that can be used as a key
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            if (keyBytes.length >= 64) { // 512 bits = 64 bytes for HS512
                return Keys.hmacShaKeyFor(keyBytes);
            }
        } catch (IllegalArgumentException e) {
            // If not a valid base64, fall back to other methods
        }
        
        // If the provided secret is not long enough, create a secure key based on it
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 64) {
            // Create a longer key by hashing the original secret
            java.security.MessageDigest sha256;
            try {
                sha256 = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hashed = sha256.digest(secret.getBytes());
                // Expand the hash to reach 512 bits minimum by creating a new array
                byte[] expandedKey = new byte[64];
                System.arraycopy(hashed, 0, expandedKey, 0, Math.min(hashed.length, 64));
                // Use the Keys utility to generate a proper key
                return Keys.hmacShaKeyFor(expandedKey); // At least 64 bytes for HS512
            } catch (java.security.NoSuchAlgorithmException e) {
                // If that fails, use a default secure key
                return Keys.secretKeyFor(SignatureAlgorithm.HS512);
            }
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}