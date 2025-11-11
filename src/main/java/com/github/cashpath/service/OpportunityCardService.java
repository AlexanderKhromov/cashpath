package com.github.cashpath.service;

import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface OpportunityCardService {

    List<OpportunityCard> getAvailableCards();

    @Transactional(readOnly = true)
    OpportunityCard getRandomAvailableCard();

    OpportunityCard markAsPurchased(Long cardId);
}
