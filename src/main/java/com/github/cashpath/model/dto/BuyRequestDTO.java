package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents request to buy an opportunity card
 */
@Schema(description = "Request payload for buying an opportunity card")
public record BuyRequestDTO(

        @Schema(
                description = "Identifier of the opportunity card to buy",
                example = "10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long cardId
) {
}