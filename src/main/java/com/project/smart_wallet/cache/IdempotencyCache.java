package com.project.smart_wallet.cache;

import com.project.smart_wallet.cache.dto.IdempotencyCacheEntry;

import java.time.Duration;

public interface IdempotencyCache {

    boolean saveIfAbsent(String idempotencyKey, IdempotencyCacheEntry idempotencyCacheEntry, Duration ttl);

    void save(String idempotencyKey, IdempotencyCacheEntry idempotencyCacheEntry, Duration ttl);

    IdempotencyCacheEntry get(String idempotencyKey);

    void delete(String idempotencyKey);

}
