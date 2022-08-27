package org.example.controllers;

import org.example.request.Auth;
import org.example.request.UserMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class AppControllerTest {

    private final AppController appController;

    @Autowired
    AppControllerTest(AppController appController) {
        this.appController = appController;
    }

    Auth auth;
    String username;
    String password;
    String invalidUsername;
    String invalidPassword;
    String invalidToken;
    String validToken;

    @BeforeEach
    void setUp() {
        auth = new Auth();
        username = "test_user1";
        password = "111";
        invalidUsername = "invalid_user";
        invalidPassword = "invalid_password";
        invalidToken = "invalidToken";
        validToken = "Bearer_eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X3VzZXIxIiwiaWF0IjoxNjYxN" +
                "TQ1OTI1LCJleHAiOjE2NjQxMzc5MjV9.7pBfjsmUPCmGWJCUPnrjeWURGQqPb1VQvxDG37uTHrU";
    }

    @AfterEach
    void tearDown() {
        auth = null;
        username = null;
        password = null;
        invalidUsername = null;
        invalidPassword = null;
    }

    @Test
    void loginWithInvalidUsername() {
        auth.setName(invalidUsername);
        assertTrue(appController.login(auth).getBody().toString().matches("Invalid username"));
    }

    @Test
    void loginWithInvalidPassword() {
        auth.setName(username);
        auth.setPassword(invalidPassword);
        assertTrue(appController.login(auth).getBody().toString().matches("Invalid password"));
    }

    @Test
    void login() {
        auth.setName(username);
        auth.setPassword(password);
        assertTrue(appController.login(auth).getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    void sendMessageWithInvalidToken() {
        assertTrue(appController.sendMessage(invalidToken, new UserMessage(username, username)).getStatusCode().equals(HttpStatus.FORBIDDEN));
    }

    @Test
    void sendMessage() {
        assertTrue(appController.sendMessage(validToken, new UserMessage(username, username)).getStatusCode().equals(HttpStatus.OK));
    }
}