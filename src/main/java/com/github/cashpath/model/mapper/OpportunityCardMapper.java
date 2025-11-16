package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.OpportunityCardDTO;
import com.github.cashpath.model.entity.OpportunityCard;
import jakarta.annotation.Nonnull;

public class OpportunityCardMapper {

    public static OpportunityCardDTO toOpportunityCardDTO(@Nonnull OpportunityCard card) {
        OpportunityCardDTO dto = new OpportunityCardDTO();
        dto.setId(card.getId());
        dto.setDescription(card.getDescription());
        dto.setPrice(card.getAmount());
        return dto;
    }
}
