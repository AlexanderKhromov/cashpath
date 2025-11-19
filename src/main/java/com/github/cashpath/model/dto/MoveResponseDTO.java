package com.github.cashpath.model.dto;

import java.util.List;

public record MoveResponseDTO(
        GameDTO game,
        PlayerDTO currentPlayer,
        List<PlayerDTO> players,
        OpportunityCardDTO card,
        boolean finished,
        String winner
) {
}
