package com.github.cashpath.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Player not found with id " + id);
    }

    public PlayerNotFoundException() {
        super("Player not found");
    }
}
