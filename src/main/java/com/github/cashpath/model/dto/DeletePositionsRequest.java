package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request to delete player's assets and liabilities")
public record DeletePositionsRequest(

        @Schema(
                description = "List of asset identifiers to delete",
                example = "[1, 2, 3]"
        )
        List<Long> assetIds,

        @Schema(
                description = "List of liability identifiers to delete",
                example = "[5, 6]"
        )
        List<Long> liabilityIds
) {
}
