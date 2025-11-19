package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.LiabilityDTO;
import com.github.cashpath.model.entity.Liability;
import jakarta.annotation.Nonnull;

public class LiabilityMapper {

    public static LiabilityDTO toLiabilityDTO(@Nonnull Liability liability) {
        return new LiabilityDTO(liability.getName(), liability.getMonthlyPayment());
    }
}
