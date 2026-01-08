package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Player financial and game state")
public record PlayerDTO(

        @Schema(description = "Player identifier", example = "2")
        Long id,

        @Schema(description = "Player name", example = "Alex")
        String name,

        @Schema(description = "Current cash balance", example = "3200.75")
        double cash,

        @Schema(description = "Player salary", example = "1500.00")
        double salary,

        @Schema(description = "Total daily cash flow", example = "350.00")
        double dailyCashFlow,

        @Schema(description = "Total daily expenses", example = "220.00")
        double dailyExpenses,

        @Schema(description = "Total passive income", example = "570.00")
        double passiveIncome,

        @Schema(description = "Player liabilities")
        Set<LiabilityDTO> liabilities,

        @Schema(description = "Player assets")
        Set<AssetDTO> assets
) {
}