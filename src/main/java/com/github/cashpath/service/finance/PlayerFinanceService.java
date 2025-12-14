package com.github.cashpath.service.finance;

import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Responsible for all calculation and financial logic
 */

@Service
public interface PlayerFinanceService {

    double getDailyCashFlow(Player player);

    double getPassiveIncome(Player player);

    void setOwner(Player player, OpportunityCard card);

    Map<Long, Double> getDailyCashFlowById(Game game);

    Player getCurrentPlayer(Game game);
}
