package com.github.cashpath.service.impl.finance;

import static org.junit.jupiter.api.Assertions.*;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.OpportunityCard;
import com.github.cashpath.model.entity.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

class PlayerFinanceServiceImplTest {

    private final PlayerFinanceServiceImpl financeService = new PlayerFinanceServiceImpl();

    // ---------- getPassiveIncome ----------

    @Test
    @DisplayName("getPassiveIncome: sums all dailyCashFlow of Assets")
    void getPassiveIncome_shouldSumAllAssetCashFlows() {
        Player player = createPlayer(10_000, 5_000, 100.0, 250.5, -50.0);

        double passive = financeService.getPassiveIncome(player);

        assertEquals(300.5, passive, 1e-6);
    }

    @Test
    @DisplayName("getPassiveIncome: returns 0, if there is no Asset")
    void getPassiveIncome_shouldBeZeroWhenNoAssets() {
        Player player = createPlayer(10_000, 5_000);

        double passive = financeService.getPassiveIncome(player);

        assertEquals(0.0, passive, 1e-6);
    }

    // ---------- getDailyCashFlow ----------

    @Test
    @DisplayName("getDailyCashFlow: general case - positive flow with round")
    void getDailyCashFlow_shouldCalculatePositiveAndRound() {
        // salary = 30_000, passive = 3_000, expenses = 10_000
        // (30_000 + 3_000 - 10_000) = 23_000
        Player player = createPlayer(30_000, 10_000, 1_000, 2_000);

        double daily = financeService.getDailyCashFlow(player);

        assertEquals(23000.0, daily);
    }

    @Test
    @DisplayName("getDailyCashFlow: negative if expenses > incomes (impossible case)")
    void getDailyCashFlow_canBeNegativeWhenExpensesHigher() {
        // salary = 20_000, passive = 0, expenses = 25_000
        // (20_000 + 0 - 25_000) = -5_000
        Player player = createPlayer(20_000, 25_000);

        double daily = financeService.getDailyCashFlow(player);

        assertEquals(-5000.0, daily);
    }

    @Test
    @DisplayName("getDailyCashFlow: for salary=0")
    void getDailyCashFlow_zeroSalary() {
        // salary = 0, passive = 3_000, expenses = 1_500
        // (0 + 3_000 - 1_500) = 1_500
        Player player = createPlayer(0, 1_500, 3_000);

        double daily = financeService.getDailyCashFlow(player);

        assertEquals(1500.0, daily);
    }

    // ---------- applyCardPurchase ----------

    @Test
    @DisplayName("applyCardPurchase: add asset to player and sets owner to asset")
    void applyCardPurchase_shouldAttachAssetToPlayerAndSetOwner() {
        Player player = createPlayer(20_000, 5_000);
        assertTrue(player.getAssets().isEmpty());

        Asset asset = new Asset();
        asset.setDailyCashFlow(500.0);

        OpportunityCard card = new OpportunityCard();
        card.setAsset(asset);

        financeService.applyCardPurchase(player, card);

        assertEquals(1, player.getAssets().size());
        Asset saved = player.getAssets().iterator().next();
        assertSame(asset, saved);
        assertSame(player, saved.getOwner());
    }

    @Test
    @DisplayName("applyCardPurchase: do nothing if card has no asset")
    void applyCardPurchase_shouldDoNothingWhenCardHasNoAsset() {
        Player player = createPlayer(20_000, 5_000);
        int before = player.getAssets().size();

        OpportunityCard card = new OpportunityCard();
        card.setAsset(null);

        financeService.applyCardPurchase(player, card);

        assertEquals(before, player.getAssets().size());
    }

    // ---------- getDailyCashFlowById ----------

    @Test
    @DisplayName("getDailyCashFlowById: returns Map id -> dailyCashFlow for all players")
    void getDailyCashFlowById_shouldReturnDailyFlowForAllPlayers() {
        Player p1 = createPlayer(30_000, 10_000, 1_000);       // passive = 1_000 -> (30+1-10)=21
        Player p2 = createPlayer(40_000, 15_000, 2_000, 1_000); // passive=3_000 -> (40+3-15)=28

        p1.setId(1L);
        p2.setId(2L);

        Game game = new Game();
        game.addPlayer(p1);
        game.addPlayer(p2);

        Map<Long, Double> result = financeService.getDailyCashFlowById(game);

        assertEquals(2, result.size());
        assertEquals(21000.0, result.get(1L));
        assertEquals(28000.0, result.get(2L));
    }

    @Test
    @DisplayName("getDailyCashFlowById: returns empty map if there are no players")
    void getDailyCashFlowById_emptyGame() {
        Game game = new Game();

        Map<Long, Double> result = financeService.getDailyCashFlowById(game);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------- helpers ----------

    private Player createPlayer(double salary, double dailyExpenses, double... assetCashFlows) {
        Player player = new Player();
        player.setSalary(salary);
        player.setDailyExpenses(dailyExpenses);

        Set<Asset> assets = new HashSet<>();
        for (double cf : assetCashFlows) {
            Asset a = new Asset();
            a.setDailyCashFlow(cf);
            assets.add(a);
        }
        player.getAssets().addAll(assets);
        return player;
    }

}