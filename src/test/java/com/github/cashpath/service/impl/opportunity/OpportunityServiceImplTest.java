package com.github.cashpath.service.impl.opportunity;

import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.opportunity.OpportunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpportunityServiceImplTest {

    @Mock
    private OpportunityCardRepository cardRepository;

    @InjectMocks
    private OpportunityServiceImpl service;

    private OpportunityCard sampleCard;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleCard = new OpportunityCard();
        sampleCard.setId(1L);
        sampleCard.setDescription("Sample Opportunity");
    }

    @Test
    @DisplayName("getRandomCard returns card from repository")
    void testGetRandomCard() {
        when(cardRepository.findRandomAvailableCard()).thenReturn(sampleCard);

        OpportunityCard result = service.getRandomCard();

        assertNotNull(result);
        assertEquals(sampleCard.getId(), result.getId());
        verify(cardRepository, times(1)).findRandomAvailableCard();
    }

    @Test
    @DisplayName("getCardOrThrow returns card when found")
    void testGetCardOrThrowFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(sampleCard));

        OpportunityCard result = service.getCardOrThrow(1L);

        assertNotNull(result);
        assertEquals(sampleCard.getId(), result.getId());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getCardOrThrow throws exception when not found")
    void testGetCardOrThrowNotFound() {
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(OpportunityCardNotFoundException.class, () -> service.getCardOrThrow(2L));

        verify(cardRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("tryMarkBought returns true when update count is 1")
    void testTryMarkBoughtSuccess() {
        when(cardRepository.markAsBoughtIfAvailable(sampleCard.getId())).thenReturn(1);

        boolean result = service.tryMarkBought(sampleCard);

        assertTrue(result);
        verify(cardRepository, times(1)).markAsBoughtIfAvailable(sampleCard.getId());
    }

    @Test
    @DisplayName("tryMarkBought returns false when update count is 0")
    void testTryMarkBoughtFailure() {
        when(cardRepository.markAsBoughtIfAvailable(sampleCard.getId())).thenReturn(0);

        boolean result = service.tryMarkBought(sampleCard);

        assertFalse(result);
        verify(cardRepository, times(1)).markAsBoughtIfAvailable(sampleCard.getId());
    }
}