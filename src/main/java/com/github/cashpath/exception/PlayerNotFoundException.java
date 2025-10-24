package com.github.cashpath.exception;

@SuppressWarnings("unused")
public class PlayerNotFoundException extends RuntimeException{

    public PlayerNotFoundException(Long id) {
        super("Player not found with id " + id);
    }

    public PlayerNotFoundException() {
        super("Player not found");
    }
}
