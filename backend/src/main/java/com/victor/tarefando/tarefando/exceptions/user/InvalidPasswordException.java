package com.victor.tarefando.tarefando.exceptions.user;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Senha inválida");
    }
}
