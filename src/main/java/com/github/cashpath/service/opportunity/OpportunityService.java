package com.github.cashpath.service.opportunity;

import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.stereotype.Service;

/**
 * Responsible for generation OpportunityCard's, buying and applying them
 */
@Service
public interface OpportunityService {

    OpportunityCard getRandomCard(Long gameId);

    OpportunityCard getCardOrThrow(Long gameId, Long id);

    boolean tryMarkBought(Long gameId, OpportunityCard card);

    void initCardsForGame(Game game);

}
