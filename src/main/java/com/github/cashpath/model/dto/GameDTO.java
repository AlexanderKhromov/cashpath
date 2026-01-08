package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Game state information")
public record GameDTO(

        @Schema(description = "Game identifier", example = "1")
        Long id,

        @Schema(description = "Current turn number", example = "7")
        int currentTurn,

        @Schema(description = "Current in-game day", example = "12")
        int currentDay
) {
}