package com.victor.tarefando.tarefando.exceptions.user;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super("Email " + email + " não encontrado");
    }
}
