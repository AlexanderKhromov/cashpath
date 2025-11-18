package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import jakarta.annotation.Nonnull;

public class MoveResponseMapper {

    public static MoveResponseDTO toMoveResponseDTO(@Nonnull Game game, @Nonnull Player player, @Nonnull OpportunityCard opportunityCard) {
        return new MoveResponseDTO(
                GameMapper.toGameDTO(game),
                PlayerMapper.toPlayerDTO(player),
                game.getPlayers().stream().map(PlayerMapper::toPlayerDTO).toList(),
                OpportunityCardMapper.toOpportunityCardDTO(opportunityCard),
                game.getStatus() == Game.GameStatus.FINISHED,
                game.getStatus() == Game.GameStatus.FINISHED ? player.getName() : null
        );
    }
}
