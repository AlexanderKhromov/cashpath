package com.github.cashpath.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player liability that causes daily expenses")
public record LiabilityDTO(

        @Schema(
                description = "Liability name",
                example = "Car loan"
        )
        String name,

        @Schema(
                description = "Daily payment amount",
                example = "45.75"
        )
        double dailyPayment
) {
}
