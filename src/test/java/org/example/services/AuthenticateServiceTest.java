package org.example.services;

import org.example.model.User;
import org.example.request.Auth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AuthenticateServiceTest {

    private final AuthenticateService authenticateService;

    @Autowired
    AuthenticateServiceTest(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    Auth auth;
    String username;
    String password;
    User user;

    @BeforeEach
    void setUp() {
        username = "test_user1";
        password = "111";
        auth = new Auth();
        auth.setName(username);
        auth.setPassword(password);
        user = new User();
        user.setPassword(password);
    }

    @AfterEach
    void tearDown() {
        auth = null;
        username = null;
        password = null;
        user = null;
    }

    @Test
    void getUser() {
        User user = authenticateService.getUser(auth);
        assertTrue(user.getUsername().matches(username) &&
                user.getPassword().matches(password));
    }

    @Test
    void validateUser() {
        assertTrue(authenticateService.validateUser(auth, user));
    }
}