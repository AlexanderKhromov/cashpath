package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.LiabilityDTO;
import com.github.cashpath.model.entity.Liability;
import jakarta.annotation.Nonnull;
import lombok.Builder;

@Builder
public class LiabilityMapper {

    public static LiabilityDTO toLiabilityDTO(@Nonnull Liability liability) {
        LiabilityDTO dto = new LiabilityDTO();
        dto.setName(liability.getName());
        dto.setMonthlyPayment(liability.getMonthlyPayment());
        return dto;
    }
}
