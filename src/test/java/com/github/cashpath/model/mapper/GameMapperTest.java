package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.GameDTO;
import com.github.cashpath.model.entity.Game;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {

    @Test
    void toGameDTO_shouldMapAllFields() {
        Game game = new Game();
        game.setId(10L);
        game.setCurrentTurn(2);
        game.setCurrentDay(1);

        GameDTO dto = GameMapper.toGameDTO(game);

        assertEquals(10L, dto.id());
        assertEquals(2, dto.currentTurn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(1, dto.currentDay());
    }
}
