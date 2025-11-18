package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.PlayerDTO;
import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.service.impl.GameServiceImpl;
import jakarta.annotation.Nonnull;

import java.util.stream.Collectors;

public class PlayerMapper {

    public static PlayerDTO toPlayerDTO(@Nonnull Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setCash(player.getCash());
        dto.setSalary(player.getSalary());
        dto.setDailyCashFlow(GameServiceImpl.getDailyCashFlow(player));
        dto.setMonthlyExpenses(player.getMonthlyExpenses());
        dto.setPassiveIncome(player.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum());
        dto.setLiabilities(player.getLiabilities().stream()
                .map(LiabilityMapper::toLiabilityDTO)
                .collect(Collectors.toSet()));
        dto.setAssets(player.getAssets().stream()
                .map(AssetMapper::toAssetDTO)
                .collect(Collectors.toSet()));
        return dto;
    }
}
