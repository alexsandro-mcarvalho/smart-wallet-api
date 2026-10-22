package com.project.smart_wallet.cache.impl;

import com.project.smart_wallet.cache.IdempotencyCache;
import com.project.smart_wallet.cache.dto.IdempotencyCacheEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisIdempotencyCache implements IdempotencyCache {

    private final RedisTemplate<String, IdempotencyCacheEntry> redisTemplate;

    @Override
    public boolean saveIfAbsent(String idempotencyKey, IdempotencyCacheEntry idempotencyCacheEntry, Duration ttl) {
        Boolean processed = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, idempotencyCacheEntry, ttl);

        if (processed == null) {
            throw new IllegalStateException("Falha ao processar operação salvar no cache");
        }

        return processed;
    }

    @Override
    public void save(String idempotencyKey, IdempotencyCacheEntry idempotencyCacheEntry, Duration ttl) {
        redisTemplate.opsForValue().set(idempotencyKey, idempotencyCacheEntry, ttl);
    }

    @Override
    public IdempotencyCacheEntry get(String idempotencyKey) {
        return redisTemplate.opsForValue().get(idempotencyKey);
    }

    @Override
    public void delete(String idempotencyKey) {
        redisTemplate.delete(idempotencyKey);
    }
}
