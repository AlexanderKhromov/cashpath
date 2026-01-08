package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Opportunity card available for purchase")
public record OpportunityCardDTO(

        @Schema(description = "Card identifier", example = "15")
        Long id,

        @Schema(
                description = "Card description",
                example = "Buy a small retail shop"
        )
        String description,

        @Schema(
                description = "Purchase price of the card",
                example = "5000.00"
        )
        double price
) {
}