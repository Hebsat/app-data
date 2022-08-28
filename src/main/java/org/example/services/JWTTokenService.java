package org.example.services;

import org.example.request.UserMessage;
import org.example.response.AuthResponse;

public interface JWTTokenService {

    AuthResponse createToken(String username);

    boolean validateToken(String token, UserMessage userMessage);
}
