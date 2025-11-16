package com.github.cashpath.controller.api;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping("/{gameId}")
    public Game getGame(@PathVariable Long gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping("/{gameId}/buy")
    public ResponseEntity<MoveResponseDTO> /*CompletableFuture<ResponseEntity<MoveResponseDTO>>*/ buy(@PathVariable Long gameId, @RequestBody BuyRequestDTO request) {
       // return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(gameService.buy(gameId, request)));
        MoveResponseDTO response = gameService.buy(gameId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/end-turn")
    public ResponseEntity<MoveResponseDTO> endTurn(@PathVariable Long gameId) {
        MoveResponseDTO response = gameService.endTurn(gameId);
        return ResponseEntity.ok(response);
    }
}
