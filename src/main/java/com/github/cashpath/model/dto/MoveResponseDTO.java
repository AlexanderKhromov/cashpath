package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Result of a game move")
public record MoveResponseDTO(

        @Schema(description = "Game state after the move")
        GameDTO game,

        @Schema(description = "Player whose turn is currently active")
        PlayerDTO currentPlayer,

        @Schema(description = "All players in the game")
        List<PlayerDTO> players,

        @Schema(description = "Opportunity card for the current turn")
        OpportunityCardDTO card,

        @Schema(description = "Whether the game is finished")
        boolean finished,

        @Schema(
                description = "Winner name if the game is finished",
                example = "Alex",
                nullable = true
        )
        String winner
) {
}