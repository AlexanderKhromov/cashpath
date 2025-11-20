package com.github.cashpath.service.impl.finance;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.service.finance.PlayerFinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class PlayerFinanceServiceImpl implements PlayerFinanceService {

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
            player.getAssets().add(asset);
        }
    }

    @Override
    public Map<Long, Double> getDailyCashFlowById(Game game) {
        return game.getPlayers().stream()
                .collect(Collectors.toMap(
                        Player::getId,
                        this::getDailyCashFlow
                ));
    }
}
