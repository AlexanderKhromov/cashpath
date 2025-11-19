package com.github.cashpath.service.finance;

import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;

/**
 * Responsible for all calculation and financial logic
 */

@Service
public interface PlayerFinanceService {

    double getDailyCashFlow(Player player);

    double getPassiveIncome(Player player);

    void applyCardPurchase(Player player, OpportunityCard card);
}
