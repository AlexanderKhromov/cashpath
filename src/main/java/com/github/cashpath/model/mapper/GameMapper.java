package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.GameDTO;
import com.github.cashpath.model.entity.Game;
import jakarta.annotation.Nonnull;

public class GameMapper {

    public static GameDTO toGameDTO(@Nonnull Game game) {
        return new GameDTO(game.getId(), game.getCurrentTurn(), game.getCurrentDay());
    }
}
