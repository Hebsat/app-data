package org.example.controllers;

import org.example.exceptions.AppInsideLoginException;
import org.example.exceptions.ErrorMessages;
import org.example.exceptions.TokenValidateException;
import org.example.model.User;
import org.example.request.Auth;
import org.example.request.UserMessage;
import org.example.services.AuthenticateService;
import org.example.services.JWTTokenService;
import org.example.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AppController {

    private final AuthenticateService authenticateService;
    private final MessageService messageService;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public AppController(AuthenticateService authenticateService, MessageService messageService, JWTTokenService jwtTokenService) {
        this.authenticateService = authenticateService;
        this.messageService = messageService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Auth auth) throws AppInsideLoginException {
        User user = authenticateService.getUser(auth);
        if (user == null) {
            throw new AppInsideLoginException(ErrorMessages.INVALID_USERNAME);
        }
        if (authenticateService.validateUser(auth, user)) {
            return ResponseEntity.status(HttpStatus.OK).body(jwtTokenService.createToken(auth.getName()));
        }
        throw new AppInsideLoginException(ErrorMessages.INVALID_PASSWORD);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String token, @RequestBody UserMessage userMessage) throws TokenValidateException {
        if (!jwtTokenService.validateToken(token, userMessage)) {
            throw new TokenValidateException(ErrorMessages.INVALID_TOKEN);
        }
        messageService.saveMessage(userMessage);
        if (messageService.messageResolver(userMessage.getMessage())) {
            return ResponseEntity.status(HttpStatus.OK).body(messageService.getHistory(userMessage.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ErrorMessages.MESSAGE_SAVED);
    }
}
