package com.github.cashpath.service.impl.admin;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.model.dto.DeletePositionsRequest;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class AdminServiceImpl implements AdminService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    @Override
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        gameRepository.delete(game);
        log.info("Game {} deleted by admin", gameId);
    }

    @Transactional
    @Override
    public void deletePositions(Long playerId, DeletePositionsRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        if (request.assetIds() != null && !request.assetIds().isEmpty()) {
            player.getAssets()
                    .removeIf(asset -> request.assetIds().contains(asset.getId()));
        }

        if (request.liabilityIds() != null && !request.liabilityIds().isEmpty()) {
            player.getLiabilities()
                    .removeIf(liability -> request.liabilityIds().contains(liability.getId()));
        }

        log.info(
                "Deleted by admin assets {} and liabilities {} for player {}",
                request.assetIds(),
                request.liabilityIds(),
                playerId
        );
    }

    @Override
    public List<Game> findAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteAllGamesByIds(List<Long> gameIds) {
        gameRepository.deleteAllById(gameIds);
    }

}
