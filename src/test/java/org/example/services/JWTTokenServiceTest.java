package org.example.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.request.UserMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JWTTokenServiceTest {

    private final JWTTokenService jwtTokenService;

    @Autowired
    JWTTokenServiceTest(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Value("${jwt.secret}")
    private String secretKey;

    String username;
    String token;
    UserMessage userMessage;

    @BeforeEach
    void setUp() {
        username = "testUser";
        userMessage = new UserMessage(username, "");
        token = "Bearer_" + Jwts.builder().setSubject(username)
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    @AfterEach
    void tearDown() {
        username = null;
    }

    @Test
    void createToken() {
        String token = jwtTokenService.createToken(username).getToken();
        assertTrue(Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token.substring(7)).getBody()
                .getSubject().matches(username));

    }

    @Test
    void validateToken() {
        assertTrue(jwtTokenService.validateToken(token, userMessage));
    }
}