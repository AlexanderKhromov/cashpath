package com.github.cashpath.config;

import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.repository.OpportunityCardRepository;
import com.github.cashpath.util.OpportunityCardGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class OpportunityCardInitializer {

    private final OpportunityCardRepository opportunityCardRepository;

    @PostConstruct
    @Transactional
    public void init() {
        long count = opportunityCardRepository.count();
        if (count == 0) {
            log.info("Table opportunity_cards is empty — initializing...");
            List<OpportunityCard> cards = OpportunityCardGenerator.generateRandomCards();
            opportunityCardRepository.saveAll(cards);
            log.info("✅ Generated and saved {} OpportunityCard.", cards.size());
        } else {
            log.info("Table opportunity_cards already contains ({} records).", count);
        }
    }

}
