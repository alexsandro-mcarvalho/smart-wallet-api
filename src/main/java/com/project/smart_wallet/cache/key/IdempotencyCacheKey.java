package com.project.smart_wallet.cache.key;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IdempotencyCacheKey {

    private final static String PREFIX = "idempotency";

    public static String buildKey(String method, String path, String userId, String idempotencyKey) {
        return PREFIX + ":" +  method + ":" + path + ":" + userId + ":" + idempotencyKey;
    }
}
