package org.example.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.request.UserMessage;
import org.example.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class JWTTokenServiceImpl implements JWTTokenService {

    @Value("${jwt.sessionTime}")
    private long sessionTime;
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public AuthResponse createToken(String username) {
        Logger.getLogger(JWTTokenServiceImpl.class.getName()).info("creating token for: " + username);
        return new AuthResponse("Bearer_" + generateToken(username));
    }

    @Override
    public boolean validateToken(String token, UserMessage userMessage) {
        Logger.getLogger(JWTTokenServiceImpl.class.getName()).info("validation token for: " + userMessage.getName());
        if (!token.startsWith("Bearer_")) {
            return false;
        }
        String jwt = token.substring(7);
        try {
            return extractAllClaims(jwt).getSubject().equals(userMessage.getName());
        } catch (Exception ex) {
            Logger.getLogger(JWTTokenServiceImpl.class.getName()).info("Token validate failed: " + ex.getMessage());
            return false;
        }
    }

    private String generateToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + sessionTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
