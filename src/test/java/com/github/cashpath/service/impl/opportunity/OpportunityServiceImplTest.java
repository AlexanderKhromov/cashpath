package com.github.cashpath.service.impl.opportunity;

import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpportunityServiceImplTest {

    @Mock
    private OpportunityCardRepository cardRepository;

    @InjectMocks
    private OpportunityServiceImpl service;

    private OpportunityCard sampleCard;
    private Game game;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        game.setId(1L);

        sampleCard = new OpportunityCard();
        sampleCard.setId(1L);
        sampleCard.setDescription("Sample Opportunity");
        sampleCard.setGame(game);
    }

    @Test
    @DisplayName("getRandomCard returns card from repository")
    void testGetRandomCard() {
        when(cardRepository.findRandomAvailableCard(game.getId())).thenReturn(List.of(sampleCard));

        OpportunityCard result = service.getRandomCard(game.getId());

        assertNotNull(result);
        assertEquals(sampleCard.getId(), result.getId());
        verify(cardRepository, times(1)).findRandomAvailableCard(game.getId());
    }

    @Test
    @DisplayName("getCardOrThrow returns card when found")
    void testGetCardOrThrowFound() {
        when(cardRepository.findByIdAndGameId(1L, game.getId())).thenReturn(Optional.of(sampleCard));

        OpportunityCard result = service.getCardOrThrow(game.getId(),1L);

        assertNotNull(result);
        assertEquals(sampleCard.getId(), result.getId());
        verify(cardRepository, times(1)).findByIdAndGameId(1L, game.getId());
    }

    @Test
    @DisplayName("getCardOrThrow throws exception when not found")
    void testGetCardOrThrowNotFound() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(OpportunityCardNotFoundException.class, () -> service.getCardOrThrow(1L,2L));

        verify(cardRepository, times(1)).findByIdAndGameId(2L, 1L);
    }

    @Test
    @DisplayName("tryMarkBought returns true when update count is 1")
    void testTryMarkBoughtSuccess() {
        when(cardRepository.markAsBoughtIfAvailable(1L, sampleCard.getId())).thenReturn(1);

        boolean result = service.tryMarkBought(1L, sampleCard);

        assertTrue(result);
        verify(cardRepository, times(1)).markAsBoughtIfAvailable(1L, sampleCard.getId());
    }

    @Test
    @DisplayName("tryMarkBought returns false when update count is 0")
    void testTryMarkBoughtFailure() {
        when(cardRepository.markAsBoughtIfAvailable(1L, sampleCard.getId())).thenReturn(0);

        boolean result = service.tryMarkBought(1L, sampleCard);

        assertFalse(result);
        verify(cardRepository, times(1)).markAsBoughtIfAvailable(1L, sampleCard.getId());
    }
}