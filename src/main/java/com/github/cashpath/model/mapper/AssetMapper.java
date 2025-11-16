package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.AssetDTO;
import com.github.cashpath.model.entity.Asset;
import jakarta.annotation.Nonnull;

public class AssetMapper {

    public static AssetDTO toAssetDTO(@Nonnull Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setName(asset.getName());
        dto.setMonthlyCashFlow(asset.getMonthlyCashFlow());
        return dto;
    }
}
