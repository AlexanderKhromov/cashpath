package com.github.cashpath.util;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityCardGeneratorTest {

    @Test
    void generateRandomCards_ShouldContainAllTypes() {
        List<OpportunityCard> cards = OpportunityCardGenerator.generateRandomCards();

        assertTrue(cards.stream().anyMatch(c -> c.getType() == OpportunityCard.OpportunityType.SMALL_DEAL));
        assertTrue(cards.stream().anyMatch(c -> c.getType() == OpportunityCard.OpportunityType.BIG_DEAL));
        assertTrue(cards.stream().anyMatch(c -> c.getType() == OpportunityCard.OpportunityType.DOODAD));
    }

    // ---------- SMALL DEAL ----------
    @Test
    void smallDeal_ShouldCreateValidAsset() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.SMALL_DEAL)
                .findFirst()
                .orElseThrow();

        Asset asset = card.getAsset();
        assertNotNull(asset);
        assertTrue(asset.getPrice() >= 5_000 && asset.getPrice() <= 30_000);
        assertTrue(asset.getMonthlyCashFlow() > 0);
        assertTrue(List.of(Asset.AssetType.REAL_ESTATE, Asset.AssetType.STOCK).contains(asset.getType()));
    }

    // ---------- BIG DEAL ----------

    @Test
    void bigDeal_ShouldCreateValidAsset() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.BIG_DEAL)
                .findFirst()
                .orElseThrow();

        Asset asset = card.getAsset();
        assertNotNull(asset);
        assertEquals(Asset.AssetType.BUSINESS, asset.getType());
        assertTrue(asset.getPrice() >= 80_000 && asset.getPrice() <= 300_000);
        assertTrue(asset.getMonthlyCashFlow() > 0);
    }

    // ---------- DOODAD ----------
    @Test
    void doodad_ShouldHavePositiveAmountWithinRangeAndNoAsset() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.DOODAD)
                .findFirst()
                .orElseThrow();

        assertNull(card.getAsset(), "DOODAD card should not have an asset");
        assertTrue(card.getAmount() >= 3_000 && card.getAmount() <= 40_000,
                "DOODAD amount should be in range 3000–40000");
    }

    // ---------- Weighted randomness ----------
    @RepeatedTest(10)
    void weightedTypeDistribution_ShouldApproximateWeights() {
        List<OpportunityCard> cards = OpportunityCardGenerator.generateRandomCards();

        long small = cards.stream().filter(c -> c.getType() == OpportunityCard.OpportunityType.SMALL_DEAL).count();
        long doodad = cards.stream().filter(c -> c.getType() == OpportunityCard.OpportunityType.DOODAD).count();
        long big = cards.stream().filter(c -> c.getType() == OpportunityCard.OpportunityType.BIG_DEAL).count();

        // Allowable deviation: ±20%
        assertTrue(small > 40);
        assertTrue(doodad > 20);
        assertTrue(big > 5);
    }

    // ---------- Regression tests ----------
    @Test
    void regression_SmallDeal_ShouldNotChangeFormula() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.SMALL_DEAL)
                .findFirst()
                .orElseThrow();

        Asset a = card.getAsset();

        // If someone changes the formula — this test will break (good!)
        double expectedRangeMin = a.getPrice() * 0.02;
        double expectedRangeMax = a.getPrice() * 0.08;

        double actualAnnual = a.getMonthlyCashFlow() * 12;

        assertTrue(actualAnnual >= expectedRangeMin);
        assertTrue(actualAnnual <= expectedRangeMax);
    }

    @Test
    void regression_BigDeal_ShouldNotChangeFormula() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.BIG_DEAL)
                .findFirst()
                .orElseThrow();

        Asset a = card.getAsset();

        // If someone changes the formula — this test will break (good!)
        double expectedRangeMin = a.getPrice() * 0.08;
        double expectedRangeMax = a.getPrice() * 0.20;

        double actualAnnual = a.getMonthlyCashFlow() * 12;

        assertTrue(actualAnnual >= expectedRangeMin);
        assertTrue(actualAnnual <= expectedRangeMax);
    }

}