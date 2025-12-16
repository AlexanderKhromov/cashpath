package com.github.cashpath.util;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.OpportunityCard;
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
        assertTrue(asset.getDailyCashFlow() > 0);
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
        assertTrue(asset.getDailyCashFlow() > 0);
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

    /**
     * deviationLimit = 1 ≈ 68% probability
     * deviationLimit = 2 ≈ 95%
     * deviationLimit = 3 ≈ 99.7%
     * deviationLimit = 4 ≈ 99.99%
     */
    @Test
    void weightedTypeDistribution_ShouldApproximateWeights() {
        int attempts = 100_000;
        int big = 0;
        for (int i = 0; i < attempts; i++) {
            OpportunityCard.OpportunityType type = OpportunityCardGenerator.getRandomWeightedType();
            if (type == OpportunityCard.OpportunityType.BIG_DEAL) {
                big++;
            }
        }

        double weight = 0.15; // BIG_WEIGHT from OpportunityCardGenerator
        double expected = attempts * weight;

        // Standard deviation for a binomial distribution (n, p)
        // Used to determine statistically acceptable deviation range.
        double stdDev = Math.sqrt(attempts * weight * (1 - weight));

        // We allow a deviation within 4 standard deviations.
        // This corresponds to a ~99.99% confidence interval,
        // making the test highly stable and resistant to random noise.
        double deviationLimit = 4 * stdDev;

        assertTrue(
                Math.abs(big - expected) <= deviationLimit,
                "Distribution is too far from expected: actual=" + big + ", expected=" + expected
        );
    }

    // ---------- Regression tests ----------
    @Test
    void regression_SmallDeal_ShouldNotChangeFormula() {
        OpportunityCard card = OpportunityCardGenerator.generateRandomCards()
                .stream()
                .filter(c -> c.getType() == OpportunityCard.OpportunityType.SMALL_DEAL)
                .findFirst()
                .orElseThrow();

        double price = card.getAsset().getPrice();
        double dailyCashFlow = card.getAsset().getDailyCashFlow();
        double roi = dailyCashFlow / price;
        assertTrue(roi >= 0.07, "ROI is lower then acceptable min");
        assertTrue(roi <= 0.12, "ROI is bigger then acceptable max");
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
        double expectedRangeMax = a.getPrice() * 0.25;

        double actualDaily = a.getDailyCashFlow();

        assertTrue(actualDaily >= expectedRangeMin);
        assertTrue(actualDaily <= expectedRangeMax);
    }

}