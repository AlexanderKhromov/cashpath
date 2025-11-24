package com.github.cashpath.service.impl.move;

import com.github.cashpath.exception.GameNotFoundException;
import com.github.cashpath.exception.PlayerNotFoundException;
import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.model.mapper.MoveResponseMapper;
import com.github.cashpath.repository.GameRepository;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.finance.PlayerFinanceService;
import com.github.cashpath.service.game.GameLifecycleService;
import com.github.cashpath.service.move.MoveFacadeService;
import com.github.cashpath.service.opportunity.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class MoveFacadeServiceImpl implements MoveFacadeService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    private final GameLifecycleService gameLifecycleService;
    private final PlayerFinanceService financeService;
    private final OpportunityService opportunityService;

    @Transactional
    @Override
    public MoveResponseDTO buy(Long gameId, BuyRequestDTO request) {

        Game game = gameLifecycleService.getGame(gameId);

        Player currentPlayer = game.getPlayers().get(game.getCurrentTurn());
        Long currentPlayerId = currentPlayer.getId();
        currentPlayer = playerRepository.findById(currentPlayerId).orElseThrow(()
                -> new PlayerNotFoundException(currentPlayerId));

        OpportunityCard card = opportunityService.getCardOrThrow(request.cardId());

        if (currentPlayer.getCash() < card.getAmount()) {
            gameLifecycleService.switchTurn(game);
            return buildMoveResponse(game);
        }

        opportunityService.tryMarkBought(card);

        if (isDeal(card)) {
            financeService.applyCardPurchase(currentPlayer, card);
        }

        double dailyFlow = financeService.getDailyCashFlow(currentPlayer);
        currentPlayer.setCash(currentPlayer.getCash() + dailyFlow - card.getAmount());
        playerRepository.save(currentPlayer);

        if (gameLifecycleService.checkWinCondition(currentPlayer)) {
            game.setStatus(Game.GameStatus.FINISHED);
        }

        gameLifecycleService.switchTurn(game);

        return buildMoveResponse(game);
    }

    @Transactional
    @Override
    public MoveResponseDTO endTurn(Long gameId) {
        Game game = gameLifecycleService.getGame(gameId);
        gameLifecycleService.switchTurn(game);
        return buildMoveResponse(game);
    }

    private MoveResponseDTO buildMoveResponse(Game game) {
        Player currentPlayer = game.getPlayers().get(game.getCurrentTurn());
        Player updatedPlayer = playerRepository.findById(currentPlayer.getId()).orElseThrow(()
                -> new PlayerNotFoundException(currentPlayer.getId()));
        OpportunityCard card = opportunityService.getRandomCard();
        Game updatedGame  = gameLifecycleService.getGame(game.getId());
        Map<Long, Double> dailyCashFlowById = financeService.getDailyCashFlowById(updatedGame);
        return MoveResponseMapper.toMoveResponseDTO(updatedGame, updatedPlayer, card, dailyCashFlowById);
    }

    private boolean isDeal(OpportunityCard card) {
        return card.getType() == OpportunityCard.OpportunityType.BIG_DEAL
                || card.getType() == OpportunityCard.OpportunityType.SMALL_DEAL;
    }
}
