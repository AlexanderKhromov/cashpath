package com.github.cashpath.service;

import com.github.cashpath.model.entity.Game;
import org.springframework.stereotype.Service;

/**
 * start/stop the game, switching moves.
 */

@Service
public interface GameService {
    String playerMove(Long gameId);

    Game createGame();
}
