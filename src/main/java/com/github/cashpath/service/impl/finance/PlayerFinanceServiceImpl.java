package com.github.cashpath.service.impl.finance;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.AssetRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.finance.PlayerFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class PlayerFinanceServiceImpl implements PlayerFinanceService {

    private final AssetRepository assetRepository;
    private final PlayerRepository playerRepository;
    public static final int MONTH_DAYS = 30;

    @Override
    public double getPassiveIncome(Player player) {
        return player.getAssets().stream()
                .mapToDouble(Asset::getMonthlyCashFlow)
                .sum();
    }

    @Override
    public double getDailyCashFlow(Player player) {
        double passive = getPassiveIncome(player);
        return Math.round((player.getSalary() + passive - player.getMonthlyExpenses()) / MONTH_DAYS);
    }

    @Transactional
    @Override
    public void applyCardPurchase(Player player, OpportunityCard card) {
        Asset asset = card.getAsset();
        if (asset != null) {
            asset.setOwner(player);
            //because entity is dirty Hibernate will save it without explicit command
            //assetRepository.save(asset);
            player.getAssets().add(asset);
        }
        //because entity is dirty Hibernate will save it without explicit command
        //playerRepository.save(player);
    }
}
