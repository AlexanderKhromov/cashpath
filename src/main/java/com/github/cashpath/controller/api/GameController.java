package com.github.cashpath.controller.api;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.service.move.MoveFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GameController {

    private final MoveFacadeService moveFacadeService;

    @PostMapping("/{gameId}/buy")
    public ResponseEntity<MoveResponseDTO> buy(@PathVariable Long gameId, @RequestBody BuyRequestDTO request) {
        MoveResponseDTO response = moveFacadeService.buy(gameId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/end-turn")
    public ResponseEntity<MoveResponseDTO> endTurn(@PathVariable Long gameId) {
        MoveResponseDTO response = moveFacadeService.endTurn(gameId);
        return ResponseEntity.ok(response);
    }
}
