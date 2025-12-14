package com.github.cashpath.service.impl.opportunity;

import com.github.cashpath.exception.OpportunityCardNotFoundException;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.service.opportunity.OpportunityService;
import com.github.cashpath.util.OpportunityCardGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Log4j2
public class OpportunityServiceImpl implements OpportunityService {
    private final OpportunityCardRepository cardRepository;

    @Override
    public OpportunityCard getRandomCard(Long gameId) {
        List<OpportunityCard> cards = cardRepository.findRandomAvailableCard(gameId);
        return cards.isEmpty() ? null : cards.getFirst();
    }

    @Override
    public OpportunityCard getCardOrThrow(Long gameId, Long id) {
        return cardRepository.findByIdAndGameId(id, gameId)
                .orElseThrow(() -> new OpportunityCardNotFoundException(gameId, id));
    }

    @Transactional
    @Override
    public boolean tryMarkBought(Long gameId, OpportunityCard card) {
        int updated = cardRepository.markAsBoughtIfAvailable(gameId, card.getId());
        return updated == 1;
    }

    @Transactional
    public void initCardsForGame(Game game) {
        List<OpportunityCard> cards = OpportunityCardGenerator.generateRandomCards();
        cards.forEach(card -> card.setGame(game));
        cardRepository.saveAll(cards);
        log.info("✅ Generated and saved {} OpportunityCard.", cards.size());
    }
}
