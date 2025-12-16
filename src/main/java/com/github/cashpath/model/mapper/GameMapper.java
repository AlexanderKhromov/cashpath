package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.GameDTO;
import com.github.cashpath.model.entity.Game;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameMapper {

    public static GameDTO toGameDTO(@Nonnull Game game) {
        return new GameDTO(game.getId(), game.getCurrentTurn(), game.getCurrentDay());
    }
}
