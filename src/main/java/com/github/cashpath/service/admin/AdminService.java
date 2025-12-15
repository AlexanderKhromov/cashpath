package com.github.cashpath.service.admin;

import com.github.cashpath.model.dto.DeletePositionsRequest;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    void deleteGame(Long gameId);

    void deletePositions(Long playerId, DeletePositionsRequest request);

    List<Game> findAllGames();

    List<Player> findAllPlayers();

    void deleteAllGamesByIds(List<Long> gameIds);

}
