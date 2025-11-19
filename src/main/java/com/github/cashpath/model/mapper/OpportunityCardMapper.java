package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.OpportunityCardDTO;
import com.github.cashpath.model.entity.OpportunityCard;
import jakarta.annotation.Nonnull;

public class OpportunityCardMapper {

    public static OpportunityCardDTO toOpportunityCardDTO(@Nonnull OpportunityCard card) {
        return new OpportunityCardDTO(card.getId(), card.getDescription(), card.getAmount());
    }
}
