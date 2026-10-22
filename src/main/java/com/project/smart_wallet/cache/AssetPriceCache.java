package com.project.smart_wallet.cache;

import com.project.smart_wallet.cache.dto.AssetPriceCacheEntry;

import java.util.Map;

public interface AssetPriceCache {

    void saveAll(Map<String, AssetPriceCacheEntry> assets);

    AssetPriceCacheEntry get(String asset);
}
