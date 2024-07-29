package com.example.spring_security_with_jwt.util;

import com.example.spring_security_with_jwt.exception.TokenValidationException;
import com.example.spring_security_with_jwt.service.JwtKeyService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final JwtKeyService jwtKeyService;

    public JwtUtil(JwtKeyService jwtKeyService) {
        this.jwtKeyService = jwtKeyService;
    }

    public String generateJwtToken(String username) {
        SecretKey secretKey = jwtKeyService.getCachedKey();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey secretKey = jwtKeyService.getCachedKey();
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenValidationException("Invalid JWT token", e);
        } catch (Exception e) {
            throw new TokenValidationException("An error occurred while validating the token", e);
        }
    }

    public String getUsernameFromToken(String token) {
        SecretKey secretKey = jwtKeyService.getCachedKey();
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
