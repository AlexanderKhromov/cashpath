package com.github.cashpath.service;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * start/stop the game, switching moves.
 */

@Service
public interface GameService {

    Game getGame(Long gameId);

    MoveResponseDTO buy(Long gameId, BuyRequestDTO request);

    Game createGame(List<Player> players);

    MoveResponseDTO endTurn(Long gameId);
}
