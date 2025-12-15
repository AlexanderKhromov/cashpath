package com.github.cashpath.service.impl.move;

import com.github.cashpath.model.dto.BuyRequestDTO;
import com.github.cashpath.model.dto.MoveResponseDTO;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.repository.PlayerRepository;
import com.github.cashpath.service.finance.PlayerFinanceService;
import com.github.cashpath.service.game.GameLifecycleService;
import com.github.cashpath.service.opportunity.OpportunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoveFacadeServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameLifecycleService gameLifecycleService;

    @Mock
    private PlayerFinanceService financeService;

    @Mock
    private OpportunityService opportunityService;

    @InjectMocks
    private MoveFacadeServiceImpl service;

    private Game game;
    private Player player;
    private OpportunityCard card;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        player = new Player();
        player.setId(1L);
        player.setCash(1000.0);

        game = new Game();
        game.setId(1L);
        game.setCurrentTurn(0);
        game.addPlayer(player);
        game.setStatus(Game.GameStatus.ACTIVE);

        card = new OpportunityCard();
        card.setId(1L);
        card.setAmount(500.0);
        card.setType(OpportunityCard.OpportunityType.BIG_DEAL);
    }

    @Test
    @DisplayName("buy skips purchase if player has insufficient cash")
    void testBuyInsufficientCash() {
        player.setCash(100.0);
        when(gameLifecycleService.getGame(1L)).thenReturn(game);
        when(opportunityService.getCardOrThrow(1L, 1L)).thenReturn(card);
        when(opportunityService.getRandomCard(1L)).thenReturn(card);
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(financeService.getCurrentPlayer(game)).thenReturn(player);
        MoveResponseDTO response = service.buy(1L, new BuyRequestDTO(1L));

        assertNotNull(response);
        verify(gameLifecycleService).switchTurn(game);
        verify(financeService, never()).setOwner(any(), any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("buy applies card purchase and updates player cash")
    void testBuySuccess() {
        when(gameLifecycleService.getGame(1L)).thenReturn(game);
        when(opportunityService.getCardOrThrow(1L, 1L)).thenReturn(card);
        when(opportunityService.tryMarkBought(1L, card)).thenReturn(true);
        when(financeService.getDailyCashFlow(player)).thenReturn(100.0);
        when(financeService.getDailyCashFlowById(game)).thenReturn(Map.of(player.getId(), 100.0));
        when(opportunityService.getRandomCard(1L)).thenReturn(card);
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(financeService.getCurrentPlayer(game)).thenReturn(player);

        MoveResponseDTO response = service.buy(1L, new BuyRequestDTO(1L));

        assertNotNull(response);
        verify(financeService).setOwner(player, card);
        verify(gameLifecycleService).switchTurn(game);
    }

    @Test
    @DisplayName("endTurn switches turn and builds response")
    void testEndTurnSuccess() {
        when(gameLifecycleService.getGame(1L)).thenReturn(game);
        when(financeService.getDailyCashFlowById(game)).thenReturn(Map.of(player.getId(), 100.0));
        when(opportunityService.getRandomCard(1L)).thenReturn(card);
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(financeService.getCurrentPlayer(game)).thenReturn(player);
        MoveResponseDTO response = service.endTurn(1L);

        assertNotNull(response);
        verify(gameLifecycleService).switchTurn(game);
    }
}