package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.PlayerDTO;
import com.github.cashpath.model.entity.Asset;
import com.github.cashpath.model.entity.Liability;
import com.github.cashpath.model.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {
    @Test
    void shouldMapPlayerToDTO() {
        Player p = new Player();
        p.setId(1L);
        p.setName("Alex");
        p.setCash(2000);
        p.setSalary(3000);
        p.setMonthlyExpenses(1500);

        Asset a = new Asset();
        a.setName("Акция");
        a.setMonthlyCashFlow(100);

        Liability l = new Liability();
        l.setName("Кредит");
        l.setMonthlyPayment(50);

        p.getAssets().add(a);
        p.getLiabilities().add(l);

        PlayerDTO dto = PlayerMapper.toPlayerDTO(p, 20);

        assertEquals("Alex", dto.name());
        assertEquals(1, dto.assets().size());
        assertEquals(1, dto.liabilities().size());
        assertEquals(100, dto.passiveIncome());
        assertEquals(20, dto.dailyCashFlow());
    }

}