package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.AssetDTO;
import com.github.cashpath.model.dto.LiabilityDTO;
import com.github.cashpath.model.dto.PlayerDTO;
import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Player;
import jakarta.annotation.Nonnull;

import java.util.Set;
import java.util.stream.Collectors;

public class PlayerMapper {

    public static PlayerDTO toPlayerDTO(@Nonnull Player player, double dailyCashFlow) {

        double passiveIncome = player.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum();
        Set<LiabilityDTO> liabilities = player.getLiabilities().stream()
                .map(LiabilityMapper::toLiabilityDTO)
                .collect(Collectors.toUnmodifiableSet());
        Set<AssetDTO> assets = player.getAssets().stream()
                .map(AssetMapper::toAssetDTO)
                .collect(Collectors.toUnmodifiableSet());
        return new PlayerDTO(
                player.getId(),
                player.getName(),
                player.getCash(),
                player.getSalary(),
                dailyCashFlow,
                player.getMonthlyExpenses(),
                passiveIncome,
                liabilities,
                assets
        );
    }
}
