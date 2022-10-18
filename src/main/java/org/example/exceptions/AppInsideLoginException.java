package org.example.exceptions;

public class AppInsideLoginException extends Exception{

    private final String message;

    public AppInsideLoginException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
