package com.devmasterteam.tasks.infra.exception;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
