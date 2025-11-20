package com.github.cashpath.service.impl.opportunity;

import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.opportunity.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class OpportunityServiceImpl implements OpportunityService {
    private final OpportunityCardRepository cardRepository;

    @Override
    public OpportunityCard getRandomCard() {
        return cardRepository.findRandomAvailableCard();
    }

    @Override
    public OpportunityCard getCardOrThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new OpportunityCardNotFoundException(id));
    }

    @Transactional
    @Override
    public boolean tryMarkBought(OpportunityCard card) {
        int updated = cardRepository.markAsBoughtIfAvailable(card.getId());
        return updated == 1;
    }
}
