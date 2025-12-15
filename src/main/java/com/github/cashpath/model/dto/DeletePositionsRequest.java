package com.github.cashpath.model.dto;

import java.util.List;

public record DeletePositionsRequest(List<Long> assetIds, List<Long> liabilityIds) {
}
