package com.github.cashpath.model.dto;

public record MoveResponseDTO(
        Long gameId,
        PlayerDTO currentPlayer,
        double dailyCashFlow,
        boolean finished,
        String winner
){}
