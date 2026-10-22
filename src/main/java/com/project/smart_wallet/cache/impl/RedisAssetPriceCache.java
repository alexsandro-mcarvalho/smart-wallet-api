package com.project.smart_wallet.cache.impl;

import com.project.smart_wallet.cache.AssetPriceCache;
import com.project.smart_wallet.cache.dto.AssetPriceCacheEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RedisAssetPriceCache implements AssetPriceCache {

    private final RedisTemplate<String, AssetPriceCacheEntry> redisTemplate;

    @Override
    public void saveAll(Map<String, AssetPriceCacheEntry> assets) {
        redisTemplate.opsForValue().multiSet(assets);
    }

    @Override
    public AssetPriceCacheEntry get(String asset) {
        return redisTemplate.opsForValue().get(asset);
    }
}
