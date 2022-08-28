package org.example.controllers;

import org.example.model.Message;
import org.example.model.User;
import org.example.request.Auth;
import org.example.request.UserMessage;
import org.example.services.AuthenticateServiceImpl;
import org.example.services.JWTTokenServiceImpl;
import org.example.services.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppControllerTest {

    private final AppController appController;

    @MockBean
    private AuthenticateServiceImpl authenticateService;
    @MockBean
    private JWTTokenServiceImpl jwtTokenService;
    @MockBean
    private MessageServiceImpl messageService;

    @Autowired
    AppControllerTest(AppController appController) {
        this.appController = appController;
    }

    @Test
    void loginWithInvalidUsername() {
        when(authenticateService.getUser(any())).thenReturn(null);
        assertTrue(appController.login(new Auth()).getBody().toString().matches("Invalid username"));
    }

    @Test
    void loginWithInvalidPassword() {
        when(authenticateService.getUser(any())).thenReturn(new User());
        when(authenticateService.validateUser(any(), any())).thenReturn(false);
        assertTrue(appController.login(new Auth()).getBody().toString().matches("Invalid password"));
    }

    @Test
    void login() {
        when(authenticateService.getUser(any())).thenReturn(new User());
        when(authenticateService.validateUser(any(), any())).thenReturn(true);
        assertEquals(appController.login(new Auth()).getStatusCode(), HttpStatus.OK);
    }

    @Test
    void sendMessageWithInvalidToken() {
        when(jwtTokenService.validateToken(any(), any())).thenReturn(false);
        assertEquals(appController.sendMessage("", new UserMessage()).getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void sendMessage() {
        when(jwtTokenService.validateToken(any(), any())).thenReturn(true);
        when(messageService.saveMessage(any())).thenReturn(new Message());
        assertEquals(appController.sendMessage("", new UserMessage()).getStatusCode(), HttpStatus.OK);
    }
}