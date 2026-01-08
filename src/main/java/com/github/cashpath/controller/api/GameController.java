package com.github.cashpath.controller.api;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.service.move.MoveFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Games API",
        description = "Operations related to game actions: buy, end turn, game flow"
)
@RestController
@RequestMapping("/api/games")
@AllArgsConstructor
public class GameController {

    private final MoveFacadeService moveFacadeService;

    @Operation(
            summary = "Buy an asset in the game",
            description = "Performs a buy action for the specified game and returns the result of the move"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Buy operation successfully executed",
                    content = @Content(schema = @Schema(implementation = MoveResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid buy request"),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PostMapping("/{gameId}/buy")
    public ResponseEntity<MoveResponseDTO> buy(
            @Parameter(description = "Game identifier", example = "1")
            @PathVariable Long gameId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Buy request payload",
                    required = true
            )
            @RequestBody BuyRequestDTO request
    ) {
        MoveResponseDTO response = moveFacadeService.buy(gameId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "End current player's turn",
            description = "Ends the current turn in the specified game and moves to the next one"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Turn successfully ended",
                    content = @Content(schema = @Schema(implementation = MoveResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @PostMapping("/{gameId}/end-turn")
    public ResponseEntity<MoveResponseDTO> endTurn(
            @Parameter(description = "Game identifier", example = "1")
            @PathVariable Long gameId
    ) {
        MoveResponseDTO response = moveFacadeService.endTurn(gameId);
        return ResponseEntity.ok(response);
    }
}
