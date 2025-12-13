package com.github.cashpath.service.game;

import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Responsible for creating game, switching moves and ending of the game
 */
@Service
public interface GameLifecycleService {


    Game getGame(Long gameId);

    Game createGame(List<Player> players);

    void switchTurn(Game game);

    boolean checkWinCondition(Player player);

    void buyCard(Player player, OpportunityCard card);
}
