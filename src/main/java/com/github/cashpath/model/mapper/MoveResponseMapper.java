package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.dto.PlayerDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

public class MoveResponseMapper {

    public static MoveResponseDTO toMoveResponseDTO(@Nonnull Game game, @Nonnull Player currentPlayer, @Nonnull OpportunityCard opportunityCard, @Nonnull Map<Long, Double> dailyCashByPlayer) {
        double currentDaily = dailyCashByPlayer.getOrDefault(currentPlayer.getId(), 0.0);
        List<PlayerDTO> players = game.getPlayers().stream()
                .map(p -> PlayerMapper.toPlayerDTO(p, dailyCashByPlayer.getOrDefault(p.getId(), 0.0)))
                .toList();
        
        return new MoveResponseDTO(
                GameMapper.toGameDTO(game),
                PlayerMapper.toPlayerDTO(currentPlayer, currentDaily),
                players,
                OpportunityCardMapper.toOpportunityCardDTO(opportunityCard),
                game.getStatus() == Game.GameStatus.FINISHED,
                game.getStatus() == Game.GameStatus.FINISHED ? currentPlayer.getName() : null
        );
    }
}
