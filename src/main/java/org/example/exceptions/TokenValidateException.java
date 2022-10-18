package org.example.exceptions;

public class TokenValidateException extends Exception{

    private final String message;

    public TokenValidateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
