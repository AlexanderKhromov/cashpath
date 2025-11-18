package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.GameDTO;
import com.github.cashpath.model.entity.Game;
import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public class GameMapper {
    public static GameDTO toGameDTO(@Nonnull Game game) {
        GameDTO dto = new GameDTO();
        dto.setId(game.getId());
        dto.setCurrentDay(game.getCurrentDay());
        dto.setCurrentTurn(game.getCurrentTurn());
        return dto;
    }
}
