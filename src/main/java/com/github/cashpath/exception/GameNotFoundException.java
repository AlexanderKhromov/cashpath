package com.github.cashpath.exception;

@SuppressWarnings("unused")
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Game not found with id " + id);
    }
}
