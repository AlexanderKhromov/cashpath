package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.OpportunityCardDTO;
import com.github.cashpath.model.entity.OpportunityCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityCardMapperTest {
    @Test
    void shouldMapCardToDTO() {
        OpportunityCard card = new OpportunityCard();
        card.setId(10L);
        card.setDescription("Купить ларёк");
        card.setAmount(3000.0);

        OpportunityCardDTO dto = OpportunityCardMapper.toOpportunityCardDTO(card);

        assertEquals(10L, dto.id());
        assertEquals("Купить ларёк", dto.description());
        assertEquals(3000.0, dto.price());
    }
}