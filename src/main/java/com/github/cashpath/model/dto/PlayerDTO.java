package com.github.cashpath.model.dto;

import java.util.Set;

public record PlayerDTO(
        Long id,
        String name,
        double cash,
        double salary,
        double dailyCashFlow,
        double monthlyExpenses,
        double passiveIncome,
        Set<LiabilityDTO> liabilities,
        Set<AssetDTO> assets

) {

}
