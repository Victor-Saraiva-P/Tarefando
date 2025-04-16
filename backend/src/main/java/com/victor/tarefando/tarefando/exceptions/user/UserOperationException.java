package com.victor.tarefando.tarefando.exceptions.user;

public class UserOperationException extends RuntimeException {
    public UserOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
