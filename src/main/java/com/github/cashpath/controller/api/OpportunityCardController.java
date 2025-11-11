package com.github.cashpath.controller.api;

import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.service.OpportunityCardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@AllArgsConstructor
public class OpportunityCardController {
    private final OpportunityCardService opportunityCardService;

    @GetMapping
    public List<OpportunityCard> getAvailableCards() {
        return opportunityCardService.getAvailableCards();
    }

    @GetMapping("/random")
    public OpportunityCard getRandomCard() {
        return opportunityCardService.getRandomAvailableCard();
    }

    @PutMapping("/{id}/purchase")
    public OpportunityCard purchaseCard(@PathVariable Long id) {
        return opportunityCardService.markAsPurchased(id);
    }
}
