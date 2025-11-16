package com.github.cashpath.model.dto;

public record MoveResponseDTO(
        Long gameId,
        String currentPlayer,
        double cash,
        double dailyCashFlow,
        OpportunityCardDTO card,
        boolean finished,
        String winner
){}
