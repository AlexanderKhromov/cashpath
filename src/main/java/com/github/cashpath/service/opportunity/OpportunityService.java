package com.github.cashpath.service.opportunity;

import com.github.cashpath.model.entity.OpportunityCard;
import org.springframework.stereotype.Service;

/**
 * Responsible for generation OpportunityCard's, buying and applying them
 */
@Service
public interface OpportunityService {

    OpportunityCard getRandomCard();

    OpportunityCard getCardOrThrow(Long id);

    void markBought(OpportunityCard card);

}
