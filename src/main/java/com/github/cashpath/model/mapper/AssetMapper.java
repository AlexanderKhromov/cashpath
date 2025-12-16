package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.AssetDTO;
import com.github.cashpath.model.entity.Asset;
import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetMapper {

    public static AssetDTO toAssetDTO(@Nonnull Asset asset) {
        return new AssetDTO(asset.getName(), asset.getDailyCashFlow());
    }
}
