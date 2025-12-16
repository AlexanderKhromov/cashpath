package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MoveResponseMapperTest {

    @Test
    void toMoveResponseDTO_shouldMapAllFieldsCorrectly() {

        // --- Prepare Game ---
        Game game = new Game();
        game.setId(1L);
        game.setCurrentTurn(3);
        game.setCurrentDay(1);
        game.setStatus(Game.GameStatus.ACTIVE);

        // --- Prepare Players ---
        Player p1 = new Player();
        p1.setId(100L);
        p1.setName("Alice");
        p1.setGame(game);

        Player p2 = new Player();
        p2.setId(200L);
        p2.setName("Bob");
        p2.setGame(game);

        game.addPlayer(p1);
        game.addPlayer(p2);

        // --- Daily cash map ---
        Map<Long, Double> daily = Map.of(
                100L, 10.0,
                200L, 25.0
        );

        // --- Opportunity card ---
        OpportunityCard card = new OpportunityCard();
        card.setId(77L);
        card.setDescription("Test card");

        // --- Call mapper ---
        MoveResponseDTO dto = MoveResponseMapper.toMoveResponseDTO(game, p2, card, daily);

        // --- Assert core fields ---
        assertEquals(1L, dto.game().id());
        assertEquals(3, dto.game().currentTurn());

        int expectedDate = 1;
        assertEquals(expectedDate, dto.game().currentDay());

        assertEquals("Bob", dto.currentPlayer().name());
        assertEquals(25.0, dto.currentPlayer().dailyCashFlow());

        assertEquals(2, dto.players().size());
        assertEquals("Alice", dto.players().get(0).name());
        assertEquals("Bob", dto.players().get(1).name());

        assertEquals("Test card", dto.card().description());

        // Game not finished
        assertFalse(dto.finished());
        assertNull(dto.winner());
    }

    @Test
    void toMoveResponseDTO_shouldSetWinnerIfGameFinished() {
        // Game finished
        Game game = new Game();
        game.setStatus(Game.GameStatus.FINISHED);

        Player current = new Player();
        current.setId(1L);
        current.setName("Winner");

        game.addPlayer(current);

        OpportunityCard card = new OpportunityCard();
        Map<Long, Double> daily = Map.of(1L, 5.0);

        MoveResponseDTO dto = MoveResponseMapper.toMoveResponseDTO(game, current, card, daily);

        assertTrue(dto.finished());
        assertEquals("Winner", dto.winner());
    }
}