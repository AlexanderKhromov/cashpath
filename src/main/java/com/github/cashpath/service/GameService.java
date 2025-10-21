package com.github.cashpath.service;

import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * start/stop the game, switching moves.
 */

@Service
public interface GameService {
    String playerMove(Long gameId);

    Game createGame(List<Player> players);

    Game findById(Long id);
}
