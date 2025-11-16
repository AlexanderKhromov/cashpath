package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.PlayerDTO;
import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import jakarta.annotation.Nonnull;

import java.util.stream.Collectors;

public class PlayerMapper {

    public static PlayerDTO toPlayerDTO(@Nonnull Player player, @Nonnull OpportunityCard opportunityCard) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setCash(player.getCash());
        dto.setSalary(player.getSalary());
        dto.setMonthlyExpenses(player.getMonthlyExpenses());
        dto.setPassiveIncome(player.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow).sum());
        dto.setLiabilities(player.getLiabilities().stream()
                .map(LiabilityMapper::toLiabilityDTO)
                .collect(Collectors.toSet()));
        dto.setAssets(player.getAssets().stream()
                .map(AssetMapper::toAssetDTO)
                .collect(Collectors.toSet()));
        dto.setCurrentOffer(OpportunityCardMapper.toOpportunityCardDTO(opportunityCard));
        return dto;
    }
}
