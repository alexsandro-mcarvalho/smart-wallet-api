package com.project.smart_wallet.cache.key;

import com.project.smart_wallet.domain.AssetType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssetPriceCacheKey {

    private final static String PREFIX = "asset-price";

    public static String buildKey(String assetIdentifier, AssetType assetType) {
        return PREFIX + ":" + assetType.name() + ":" + assetIdentifier.toLowerCase();
    }
}
