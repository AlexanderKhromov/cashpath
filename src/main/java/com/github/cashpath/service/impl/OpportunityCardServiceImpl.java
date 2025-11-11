package com.github.cashpath.service.impl;

import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.OpportunityCardService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
@Log4j2
public class OpportunityCardServiceImpl implements OpportunityCardService {
    private final OpportunityCardRepository opportunityCardRepository;

    public List<OpportunityCard> getAvailableCards() {
        return opportunityCardRepository.findByIsAvailableTrue();
    }

    @Transactional(readOnly = true)
    @Override
    public OpportunityCard getRandomAvailableCard() {
        List<OpportunityCard> available = getAvailableCards();
        if (available.isEmpty()) {
            log.warn("Нет доступных карт возможностей");
            return null;
        }
        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }

    @Override
    public OpportunityCard markAsPurchased(Long cardId) {
        OpportunityCard card = opportunityCardRepository.findById(cardId)
                .orElseThrow(() -> new OpportunityCardNotFoundException(cardId));
        card.markAsUnavailable();
        return opportunityCardRepository.save(card);
    }
}
