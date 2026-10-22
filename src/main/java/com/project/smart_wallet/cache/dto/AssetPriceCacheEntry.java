package com.project.smart_wallet.cache.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AssetPriceCacheEntry(BigDecimal price, Instant updatedAt) {
}
