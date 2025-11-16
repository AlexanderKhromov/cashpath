package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import jakarta.annotation.Nonnull;

public class MoveResponseMapper {

    public static MoveResponseDTO toMoveResponseDTO(@Nonnull Game game, String playerName, double cash, double dailyCashFlow, @Nonnull OpportunityCard opportunityCard) {
        return new MoveResponseDTO(
                game.getId(),
                playerName,
                cash,
                dailyCashFlow,
                OpportunityCardMapper.toOpportunityCardDTO(opportunityCard),
                game.getStatus() == Game.GameStatus.FINISHED,
                        game.getStatus() == Game.GameStatus.FINISHED? playerName:null
        );
    }
}
