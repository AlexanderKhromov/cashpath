package com.github.cashpath.service.impl.finance;

import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Game;
import com.github.cashpath.model.entity.Player;
import com.github.cashpath.service.finance.PlayerFinanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PlayerFinanceServiceIntegrationTest {
    @Autowired
    private PlayerFinanceService financeService;

    @Test
    @DisplayName("Integration: bean PlayerFinanceService is up and calculate dailyCashFlow with real entity")
    void contextLoads_andDailyCashFlowWorks() {
        Player p1 = new Player();
        p1.setId(1L);
        p1.setSalary(30_000);
        p1.setDailyExpenses(10_000);

        Asset a1 = new Asset();
        a1.setDailyCashFlow(1_000);
        a1.setOwner(p1);
        p1.getAssets().add(a1);

        Game game = new Game();
        game.addPlayer(p1);

        Map<Long, Double> result = financeService.getDailyCashFlowById(game);

        assertEquals(1, result.size());
        assertEquals(21000.0, result.get(1L)); // (30+1-10)=21
    }
}
