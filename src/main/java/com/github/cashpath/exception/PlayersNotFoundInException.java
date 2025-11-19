package com.github.cashpath.exception;

public class PlayersNotFoundInException extends RuntimeException {

    public PlayersNotFoundInException(Long gameId) {
        super("The are no players found in the game with id " + gameId);
    }
}
