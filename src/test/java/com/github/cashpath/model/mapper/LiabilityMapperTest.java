package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.LiabilityDTO;
import com.github.cashpath.model.entity.Liability;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiabilityMapperTest {

    @Test
    void toLiabilityDTO_shouldMapFields() {
        Liability liability = new Liability();
        liability.setName("Credit Card");
        liability.setDailyPayment(150.75);

        LiabilityDTO dto = LiabilityMapper.toLiabilityDTO(liability);

        assertEquals("Credit Card", dto.name());
        assertEquals(150.75, dto.dailyPayment());
    }
}

