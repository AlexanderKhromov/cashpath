package com.github.cashpath.controller.api;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GameController {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameService gameService;
/*
    @PostMapping
    public Game createGame() {
        return gameService.createGame();
    }
 */

    @PostMapping("/{gameId}/players")
    public Player addPlayer(@PathVariable Long gameId, @RequestBody Map<String, String> body) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        Player player = new Player();
        player.setName(body.get("name"));
        player.setCash(1000);
        player.setSalary(500);
        player.setMonthlyExpenses(200);
        player = playerRepository.save(player);

        game.getPlayers().add(player);
        gameRepository.save(game);
        return player;
    }

    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
    }

    @PostMapping("/{gameId}/move")
    public ResponseEntity<Void> move(@PathVariable Long gameId) {
        gameService.playerMove(gameId);
        return ResponseEntity.ok().build();
    }

}
