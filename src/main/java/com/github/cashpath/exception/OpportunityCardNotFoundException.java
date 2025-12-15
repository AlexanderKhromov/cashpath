package com.github.cashpath.exception;

public class OpportunityCardNotFoundException extends RuntimeException {
    public OpportunityCardNotFoundException(Long gameId, Long id) {
        super("OpportunityCard not found: id " + id + " game id " + gameId);
    }
}
