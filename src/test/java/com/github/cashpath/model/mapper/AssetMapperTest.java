package com.github.cashpath.model.mapper;

import com.github.cashpath.model.dto.AssetDTO;
import com.github.cashpath.model.entity.Asset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetMapperTest {
    @Test
    void shouldMapAssetToDTO() {
        Asset asset = new Asset();
        asset.setName("Квартира");
        asset.setDailyCashFlow(500);

        AssetDTO dto = AssetMapper.toAssetDTO(asset);

        assertEquals("Квартира", dto.name());
        assertEquals(500, dto.dailyCashFlow());
    }
}