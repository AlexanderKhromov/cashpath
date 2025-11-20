package com.github.cashpath.service.impl.opportunity;

import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.opportunity.OpportunityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OpportunityServiceIntegrationTest {
    @Autowired
    OpportunityCardRepository repository;

    @Autowired
    OpportunityService service;

    @Test
    void shouldMarkCardBoughtAtomically() {
        OpportunityCard card = new OpportunityCard();
        card.setType(OpportunityCard.OpportunityType.SMALL_DEAL);
        card.setDescription("Test");
        repository.save(card);

        boolean result1 = service.tryMarkBought(card);
        boolean result2 = service.tryMarkBought(card);

        assertTrue(result1);
        assertFalse(result2, "Карта не должна покупаться повторно");
    }
}