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
        LocalDate localDate = LocalDate.of(2025, 11, 20);
        Game game = new Game();
        game.setId(10L);
        game.setCurrentTurn(2);
        game.setCurrentDay(localDate);

        GameDTO dto = GameMapper.toGameDTO(game);

        assertEquals(10L, dto.id());
        assertEquals(2, dto.currentTurn());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expectedDate = LocalDate.parse("2025-11-20", formatter);
        assertEquals(expectedDate, dto.currentDay());
    }
}
